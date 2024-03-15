<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>
<script setup>
import BasicLayout from "@/layouts/BasicLayout";
import { useRoute } from "vue-router";
const route = useRoute();
console.log(route.path);
</script>
<style></style>
