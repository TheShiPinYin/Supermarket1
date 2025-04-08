<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.orderNo" placeholder="请输入订单编号查询" :prefix-icon="Search"></el-input>
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入商品名称查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
                :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单编号" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="count" label="商品数量" />
        <el-table-column prop="total" label="订单总价" />
        <el-table-column prop="supplierName" label="供货商" />
        <el-table-column prop="time" label="订单时间" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
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

    <el-dialog title="订单信息" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="orderNo" label="订单编号">
          <el-input v-model="data.form.orderNo" autocomplete="off" placeholder="请输入订单编号"/>
        </el-form-item>
        <el-form-item prop="name" label="商品名称">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入商品名称"/>
        </el-form-item>
        <el-form-item prop="count" label="商品数量">
          <el-input v-model="data.form.count" autocomplete="off" placeholder="请输入商品数量"/>
        </el-form-item>
        <el-form-item prop="total" label="订单总价">
          <el-input v-model="data.form.total" autocomplete="off" placeholder="请输入订单总价"/>
        </el-form-item>
        <el-form-item prop="supplierId" label="供货商">
          <el-select v-model="data.form.supplierId" placeholder="请选择供货商" style="width: 100%">
            <el-option v-for="item in data.supplierData" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="time" label="订单时间">
          <el-date-picker style="width: 100%"
                          v-model="data.form.time"
                          type="date"
                          placeholder="请选择日期"
                          value-format="YYYY-MM-DD"
          />
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

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  orderNo: '',
  name: '',
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    orderNo: [
      { required: true, message: '请填写订单编号', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请填写商品名称', trigger: 'blur' }
    ],
    count: [
      { required: true, message: '请填写商品数量', trigger: 'blur' }
    ],
    total: [
      { required: true, message: '请填写订单总价', trigger: 'blur' }
    ],
    supplierId: [
      { required: true, message: '请选择供货商', trigger: 'blur' }
    ],
    time: [
      { required: true, message: '请选择订单时间', trigger: 'blur' }
    ]
  },
  rows: [],
  ids: [],
  supplierData: []
})

const formRef = ref()

const load = () => {
  request.get('/orders/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderNo: data.orderNo,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadSupplier = () => {
  request.get('/supplier/selectAll').then(res => {
    if (res.code === '200') {
      data.supplierData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()
loadSupplier()

const reset = () => {
  data.orderNo = ''
  data.name = ''
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/orders/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.put('/orders/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/orders/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {  // rows 就是实际选择的数组
  data.rows = rows
  data.ids = data.rows.map(v => v.id) // map可以把对象的数组 转换成一个纯数字的数组
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/orders/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

</script>