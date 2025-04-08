<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.name" placeholder="请输入分类名称查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
      <el-button type="info" @click="exportData">批量导出</el-button>
      <el-upload
          style="display: inline-block; margin-left: 10px"
          action="http://localhost:8080/category/import"
          :show-file-list="false"
          :on-success="handleImportSuccess"
      >
        <el-button type="success">批量导入</el-button>
      </el-upload>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
                :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="name" label="分类名称"/>
        <el-table-column prop="content" label="分类说明"/>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="dele(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <el-dialog title="商品分类信息" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="name" label="分类名称">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入分类名称"/>
        </el-form-item>
        <el-form-item prop="content" label="分类说明">
          <el-input v-model="data.form.content" autocomplete="off" placeholder="请输入分类说明"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { saveAs } from 'file-saver';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  name: '',
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    name: [
      { required: true, message: '请输入分类名称', trigger: 'blur' },
    ]
  },
  rows: [],
  ids: []
});

const formRef = ref()

const load = () => {
  request.get('/category/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list;
      data.total = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};
load();

const reset = () => {
  data.name = '';
  load();
};

const handleAdd = () => {
  data.formVisible = true;
  data.form = {};
};

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/category/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false;
          ElMessage.success('新增成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/category/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false;
          ElMessage.success('修改成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

const save = () => {
  data.form.id ? update() : add();
};

const dele = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/category/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(err => {});
};

const handleSelectionChange = (rows) => {
  data.rows = rows;
  data.ids = data.rows.map(v => v.id);
};

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据');
    return;
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/category/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(err => {});
};

const exportData = () => {
  let idsStr = data.ids.join(",");
  let url = `http://localhost:8080/category/export?name=${data.name === null ? '' : data.name}`
      + `&ids=${idsStr}`
      + `&token=${data.user.name}`;
  window.open(url);
};

const handleImportSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量导入数据成功');
    load();
  } else {
    ElMessage.error(res.msg);
  }
};
</script>