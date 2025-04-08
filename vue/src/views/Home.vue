<template>
  <div class="container">
    <div class="left-section" style="margin-top: 10px; width: 65%;">
      <div style="margin-bottom: 20px">
        <el-carousel trigger="click" height="450px" style="">
          <el-carousel-item v-for="item in data.carouselData" :key="item">
            <img :src="item" alt="" style="height: auto; width: 100%">
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="info-section card" style="border: 1px solid #e4e7ed; border-radius: 4px; padding: 15px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);">
        <div>欢迎使用超市会员管理系统</div>
        <div class="card" style="margin-bottom: 5px">
          <el-input v-model="data.content" placeholder="请输入留言内容" style="width: 80%; margin-right: 5px"></el-input>
          <el-button type="primary" @click="addMessage">留 言</el-button>
        </div>

        <div class="card">
          <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="content" label="留言内容" />
            <el-table-column prop="time" label="留言时间" />
          </el-table>
        </div>
      </div>
    </div>
    <div class="right-section card" style="margin-top: 10px; width: 35%; border: 1px solid #e4e7ed; border-radius: 4px; padding: 15px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);">
      <div style="font-size: 18px; margin-bottom: 20px">系统公告</div>
      <el-timeline style="max-width: 100%;">
        <el-timeline-item :timestamp="item.time" color="#0bbd87" placement="top" v-for="item in data.noticeData">
          <h4>{{ item.title }}</h4>
          <p>{{ item.content }}</p>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import lun1 from '@/assets/imgs/lun-1.jpg';
import lun2 from '@/assets/imgs/lun-2.jpg';
import lun3 from '@/assets/imgs/lun-3.jpg';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  noticeData: [],
  carouselData: [lun1, lun2, lun3],
  content: '',
  tableData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
      if (data.noticeData.length > 5) {
        data.noticeData = data.noticeData.slice(0, 5)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const addMessage = () => {
  const user = JSON.parse(localStorage.getItem('code_user') || '{}');
  const message = {
    content: data.content,
    username: user.username,
    time: new Date().toLocaleString()
  };
  request.post('/message/add', message).then(res => {
    if (res.code === '200') {
      ElMessage.success('留言成功');
      data.content = '';
      loadMessages();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const loadMessages = () => {
  request.get('/message/selectAll').then(res => {
    if (res.code === '200') {
      data.tableData = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

onMounted(() => {
  loadMessages();
});
loadNotice()
</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.info-section {
  margin-top: 10px;
}
</style>

