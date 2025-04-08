<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input prefix-icon="Search" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入商品名称查询"></el-input>
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
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="img" label="商品图片">
          <template v-slot="scope">
            <el-image style="width: 60px; height: 60px; border-radius: 10px" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称"/>
        <el-table-column prop="price" label="商品价格"/>
        <el-table-column prop="categoryName" label="商品分类"/>
        <el-table-column prop="supplierName" label="供货商"/>
        <el-table-column prop="count" label="商品库存"/>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" :icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)"></el-button>
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

    <el-dialog width="35%" v-model="data.formVisible" title="商品信息" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="商品图片" prop="picture">
          <el-upload
              class="upload-demo"
              :action="'http://localhost:8080/files/upload'"
              list-type="picture"
              :on-success="handleFileUpload"
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input v-model="data.form.price" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <el-select v-model="data.form.categoryId" placeholder="请选择商品分类" style="width: 100%">
            <el-option v-for="item in data.categoryData" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="供货商" prop="supplierId">
          <el-select v-model="data.form.supplierId" placeholder="请选择供货商" style="width: 100%">
            <el-option v-for="item in data.supplierData" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="商品库存" prop="count">
          <el-input v-model="data.form.count" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request";
import { Delete, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  name: null,
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  formVisible: false,
  form: {},
  categoryData: [],
  supplierData: [],
  rows: [],
  ids: []
})

const rules = reactive({
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
  ]
})

const formRef = ref()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleFileUpload = (res) => {
  data.form.img = res.data
  console.log(data.form.img)
}

const loadCategory = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categoryData = res.data
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

const update = () => {
  request.put('/goods/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const add = () => {
  request.post('/goods/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/goods/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {})
}

const load = () => {
  request.get('/goods/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.name = null
  load()
}

const handleSelectionChange = (rows) => {
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/goods/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

loadCategory()
loadSupplier()
load()
</script>