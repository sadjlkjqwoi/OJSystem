import { StoreOptions } from "vuex";
import ACCESS_ENUM from '@/access/accessEnum';
import { UserControllerService } from "@/generated";
export default{

    namespaced:true,
    state:()=>({
        loginUser:{
            userName:"未登录"
        },
    }),
    actions:{
        async getLoginUser({commit,state},payload){
            const res=await UserControllerService.getLoginUserUsingGet();
            console.log(res.code)
            if(res.code===0){
                commit("updateUser",res.data);
            }
            else
            {
           
                commit("updateUser",{...state.loginUser,userName:"未登录",userRole:ACCESS_ENUM.NOT_LOGIN})
                
            }
            //todo 改为远程请求获取登录信息
        },
     
    },
    mutations:{
        updateUser(state,payload){
            state.loginUser=payload;
        },
 
    },
} as StoreOptions<any>;