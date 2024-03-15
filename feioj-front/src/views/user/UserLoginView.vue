<template>
  <div id="userLoginView">
    <h2>用户登录</h2>
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
      <a-form-item>
        <a-button type="primary" html-type="submit">登录</a-button>
       
      </a-form-item>
       <a @click="register">还没注册？快点来注册吧</a>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { UserControllerService, UserLoginRequest } from "@/generated";
import store from "../../store";
const router = useRouter();
/**
 * 表单信息
 */
const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);
/**
 * 表单提交
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  if (res.code === 0) {
    await store.dispatch("user/getLoginUser");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登录失败" + res.message);
  }
};
const register = async () => {
    router.push({
      path: "/user/register",
      replace: true,
    });

};
</script>


