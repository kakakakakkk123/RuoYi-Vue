<template>
  <el-dialog :title="title" :visible.sync="visible" :width="width" append-to-body @close="handleClose">
    <el-upload
      ref="uploadRef"
      :limit="1"
      accept=".xlsx, .xls"
      :headers="headers"
      :action="uploadUrl"
      :disabled="isUploading"
      :on-progress="handleProgress"
      :on-success="handleSuccess"
      :auto-upload="false"
      drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip text-center" slot="tip">
        <div class="el-upload__tip">
          <el-checkbox v-model="updateSupport">{{ updateSupportLabel }}</el-checkbox>
        </div>
        <div class="el-upload__tip" v-if="extraSupportLabel">
          <el-checkbox v-model="extraSupport">{{ extraSupportLabel }}</el-checkbox>
        </div>
        <span>仅允许导入xls、xlsx格式文件。</span>
        <el-link v-if="templateUrl" type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="handleDownloadTemplate">下载模板</el-link>
      </div>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  props: {
    title: {
      type: String,
      default: '数据导入'
    },
    width: {
      type: String,
      default: '400px'
    },
    action: {
      type: String,
      required: true
    },
    templateAction: {
      type: String,
      default: ''
    },
    templateFileName: {
      type: String,
      default: 'template'
    },
    updateSupportLabel: {
      type: String,
      default: '是否更新已经存在的数据'
    },
    extraSupportLabel: {
      type: String,
      default: ''
    },
    extraSupportParam: {
      type: String,
      default: 'extraSupport'
    }
  },
  data() {
    return {
      visible: false,
      isUploading: false,
      updateSupport: false,
      extraSupport: false,
      headers: { Authorization: 'Bearer ' + getToken() }
    }
  },
  computed: {
    uploadUrl() {
      let url = process.env.VUE_APP_BASE_API + this.action + '?updateSupport=' + (this.updateSupport ? 1 : 0)
      if (this.extraSupportLabel) {
        url += '&' + this.extraSupportParam + '=' + (this.extraSupport ? 1 : 0)
      }
      return url
    },
    templateUrl() {
      return !!this.templateAction
    }
  },
  methods: {
    open() {
      this.updateSupport = false
      this.extraSupport = false
      this.isUploading = false
      this.visible = true
      this.$nextTick(() => {
        if (this.$refs.uploadRef) {
          this.$refs.uploadRef.clearFiles()
        }
      })
    },
    handleClose() {
      this.isUploading = false
      if (this.$refs.uploadRef) {
        this.$refs.uploadRef.clearFiles()
      }
    },
    handleDownloadTemplate() {
      this.download(this.templateAction, {}, `${this.templateFileName}_${new Date().getTime()}.xlsx`)
    },
    handleProgress() {
      this.isUploading = true
    },
    handleSuccess(response) {
      this.visible = false
      this.isUploading = false
      if (this.$refs.uploadRef) {
        this.$refs.uploadRef.clearFiles()
      }
      this.$alert("<div style='overflow:auto;overflow-x:hidden;max-height:70vh;padding:10px 20px 0;'>" + response.msg + '</div>', '导入结果', { dangerouslyUseHTMLString: true })
      this.$emit('success', response)
    },
    handleSubmit() {
      const files = this.$refs.uploadRef.uploadFiles
      if (!files || files.length === 0) {
        this.$modal.msgError('请选择要上传的文件')
        return
      }
      const name = files[0].name.toLowerCase()
      if (!name.endsWith('.xls') && !name.endsWith('.xlsx')) {
        this.$modal.msgError('请选择后缀为"xls"或"xlsx"的文件')
        return
      }
      this.$refs.uploadRef.submit()
    }
  }
}
</script>
