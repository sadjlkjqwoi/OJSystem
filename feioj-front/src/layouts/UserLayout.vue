<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header @click="go" class="header">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.jpeg" />
          <div class="title">MAR OJ</div>
        </div>
      </a-layout-header>
      <a-layout-content class="content"> <router-view /></a-layout-content>
      <a-layout-footer class="footer" @click="go">OJ 判题系统</a-layout-footer>
    </a-layout>
  </div>
</template>
<script setup>
import { useRouter } from "vue-router";

const router = useRouter();
const go = async () => {
    router.push({
      path: "/",
      replace: true,
    });

};
</script>

<style scoped>
#userLayout {
  margin: auto 0;
}
#userLayout .header {
}
#userLayout .content {
  align-content: center;
  margin-top: 10px;
  padding: 20px;
  text-align: center;
}
#userLayout .footer {
  position: sticky;
  right: 0;
  bottom: 0px;
  left: 0px;
  padding: 13px;
  text-align: center;
  background: #efefef;
}
.logo {
  width: 64px;
  height: 64px;
}
.title-bar {
  display: flex;
  align-items: center;
}
</style>
