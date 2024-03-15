<template>
  <div id="userRegisterView">
    <h2>用户注册</h2>
    <a-form
      label-align="left"
      auto-label-width
      style="max-width: 480px; margin: 0 auto"
      :model="form"
      @submit="handleSubmit">
      <a-form-item field="userAccount" label="账号">
        <a-input
          v-model="form.userAccount"
          tooltip="账号不少于6位"
          placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码">
        <a-input-password
          v-model="form.userPassword"
          type="password"
          placeholder="请输入密码" />
      </a-form-item>
       <a-form-item field="checkPassword" label="重复密码">
        <a-input-password
          v-model="form.checkPassword"
          type="password"
          placeholder="请再次输入密码" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { UserControllerService, UserRegisterRequest } from "@/generated";
const router = useRouter();
/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
  checkPassword:'',
} as UserRegisterRequest);
/**
 * 表单提交
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userRegisterUsingPost(form);
  if (res.code === 0) {
 
    router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("注册失败" + res.message);
  }
};
</script>

<style></style>
