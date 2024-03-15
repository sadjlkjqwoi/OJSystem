import UserLayoutVue from "@/layouts/UserLayout.vue";

import {  RouteRecordRaw } from 'vue-router'
import NoAuth from '../views/NoAuth.vue'
import UserLayout from '@/layouts/UserLayout.vue'
import UserLoginView from '../views/user/UserLoginView.vue'
import UserRegisterView from '../views/user/UserRegisterView.vue'
import ACCESS_ENUM from '@/access/accessEnum'
import AddQuestionView from'../views/question/AddQuestionView.vue'
import  ManageQuestionView from'../views/question/ManageQuestionView.vue'
import QuestionsView from'../views/question/QuestionsView.vue'
import ViewQuestionView from'../views/question/ViewQuestionView.vue'
import  QuestionSubmitView from'../views/question/QuestionSubmitView.vue'
export const routes: Array<RouteRecordRaw> = [
  {
    path: '/user',
    name: '用户页面',
    component: UserLayout,
    meta:{
      hideInMenu:true
    },
    children:[
      {
        path: '/user/login',
        name: '登录页面',
        component: UserLoginView
      },
      {
        path: '/user/register',
        name: '注册页面',
        component: UserRegisterView,
      },
    ]
  },
    {
      path: '/',
      name: '主页',
      component: QuestionsView
    },
    {
      path: "/questions",
      name: "浏览题目",
      component: QuestionsView,
    },
    {
      path: "/question_submit",
      name: "浏览题目提交",
      component: QuestionSubmitView,
      meta: {
        access: ACCESS_ENUM.USER,
      },
    },
    {
      path: "/add/question",
      name: "创建题目",
      component: AddQuestionView,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: "/update/question",
      name: "更新题目",
      component: AddQuestionView,
      meta:{
        hideInMenu:true
      }
    },
    {
      path: "/view/question/:id",
      name: "在线做题",
      component: ViewQuestionView,
      props: true,
      meta: {
        access: ACCESS_ENUM.USER,
        hideInMenu: true,
      },
    },
    {
      path: "/manage/question/",
      name: "管理题目",
      component: ManageQuestionView,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: '/noAuth',
      name: '没有权限',
      component: NoAuth,
      meta:{
        hideInMenu:true
      }
    },

  ]