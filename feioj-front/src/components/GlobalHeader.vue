<template>
  <a-row
    id="globalHeader"
    style="margin-bottom: 16px"
    align="center"
    :wrap="false">
    <a-col flex="auto">
      <a-menu mode="horizontal" :default-selected-keys="['1']">
        <a-menu-item
          @click="go"
          key="selectedKey"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled>
          <div class="title-bar">
            <img class="logo" src="../assets/logo.jpeg" />
            <div class="title">MAR OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item
          v-for="item in visibleRoutes"
          :key="item.path"
          @click="doMenuClick(item.path)"
          >{{ item.name }}</a-menu-item
        >
      </a-menu>
    </a-col>
        <a-col flex="100px">
        <div><a v-if="store.state.user.loginUser.userName!='未登录'" @click="logout">注销</a></div>
    </a-col>
  
    <a-col flex="100px">
      <div v-if="store.state.user.loginUser.userName=='未登录'" @click="login">去登录</div>
        <div v-else>{{ store.state.user.loginUser.userName }}</div>
    </a-col>
  </a-row>

</template>

<script setup>
import { routes } from "@/router/routes";
import { computed, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";
import { UserControllerService } from "../generated";
const router = useRouter();
const route = useRoute();
const selectedKeys = ref(route.path);
const doMenuClick = (key) => {
  router.push({
    path: key,
  });
};
const store = useStore();
// setTimeout(() => {
//   store.dispatch("user/getLoginUser", {
//     userName: "黑煤球管理员 ",
//     role: ACCESS_ENUM.ADMIN,
//   });
// }, 3000);
const loginUser = store.state.user.loginUser;
console.log("用户名：" + loginUser.userName + "," + loginUser.userRole);
// 展示在菜单的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      console.log(item.name);
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(store.state.user.loginUser, item?.meta?.access)) {
      return false;
    }
    return true;
  });
});


const logout = async () => {
  const res = await UserControllerService.userLogoutUsingPost();
  if (res.code === 0) {
     await store.dispatch("user/getLoginUser");
    router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("注销失败" + res.message);
  }
};
const login = async () => {
 
    router.push({
      path: "/user/login",
      replace: true,
    });

};

</script>
<style scoped>
.logo {
  width: 48px;
}
.title-bar {
  display: flex;
  align-items: center;
}
.title {
  margin-left: 16px;
  color: #444;
}
</style>
