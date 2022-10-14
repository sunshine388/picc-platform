package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.common.tokenUtil;
import com.snowyyy.picc.entity.AdminMenu;
import com.snowyyy.picc.entity.Manager;
import com.snowyyy.picc.mapper.AdminMenuMapper;
import com.snowyyy.picc.mapper.ManagerMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/manager")
@Api(tags = "管理中心接口")
public class AdminManagerController {

    @Autowired
    private ManagerMapper adminManagerMapper;
    @Autowired
    private AdminMenuMapper adminMenuMapper;

    @ApiOperation(value = "后台管理用户获取菜单列表")
    @GetMapping("/get_menulist")
    @ResponseBody
    public GlobalResult get_menulist() {
        JSONArray data=new JSONArray();
        List<AdminMenu> menulist=this.adminMenuMapper.selectList(new QueryWrapper<AdminMenu>().eq("menu_level", 1));
        for(AdminMenu menu:menulist){
            JSONObject object=new JSONObject();
            object.put("id",menu.getId());
            object.put("authName",menu.getMenuName());
            object.put("path",menu.getMenuPath());
            object.put("order",menu.getMenuOrder());
            JSONArray childrenArray=new JSONArray();
            List<AdminMenu> clildrenlist=this.adminMenuMapper.selectList(new QueryWrapper<AdminMenu>().eq("menu_pid", menu.getId()));
            for(AdminMenu child:clildrenlist){
                JSONObject objectchild=new JSONObject();
                objectchild.put("id",child.getId());
                objectchild.put("authName",child.getMenuName());
                objectchild.put("path",child.getMenuPath());
                objectchild.put("order",child.getMenuOrder());
                objectchild.put("children",null);
                childrenArray.add(objectchild);
            }
            object.put("children",childrenArray);
            data.add(object);
        }
        String message = "获取菜单列表成功";
        GlobalResult result = GlobalResult.build(200, message, data);
        return result;
    }
    @ApiOperation(value = "后台管理用户登录")
    @PostMapping("/manager_login")
    @ResponseBody
    public GlobalResult manager_login(@RequestParam(value = "username", required = false) String username,
                                      @RequestParam(value = "password", required = false) String password) {
        Manager adminManager = this.adminManagerMapper.selectOne(new QueryWrapper<Manager>().eq("mg_name", username));
        if (adminManager == null) {
            String message = "用户名错误";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
                if (adminManager.getPassWord().equals(password)) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    String token = tokenUtil.token(username, password);
                    data.put("username", username);
                    data.put("token", token);
                    data.put("state",adminManager.getState());
                    String message = "登录成功";
                    GlobalResult result = GlobalResult.build(200, message, data);
                    return result;
                } else {
                    String message = "密码错误";
                    GlobalResult result = GlobalResult.build(400, message, null);
                    return result;
                }
        }
    }
    @ApiOperation(value = "后台管理用户修改密码")
    @PostMapping("/manager_updatePassword")
    @ResponseBody
    public GlobalResult manager_updatePassword(@RequestParam(value = "username", required = true) String username,
                                      @RequestParam(value = "oriPassword", required = true) String oriPassword,
                                                           @RequestParam(value = "newPassword", required = true) String newPassword) {
        Manager adminManager = this.adminManagerMapper.selectOne(new QueryWrapper<Manager>().eq("mg_name", username));
        if (adminManager == null) {
            String message = "用户名错误";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            if (adminManager.getPassWord().equals(oriPassword)) {
                adminManager.setPassWord(newPassword);
                adminManagerMapper.updateById(adminManager);
                String message = "修改密码成功";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            } else {
                String message = "原密码输入错误";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }
    }
    @ApiOperation(value = "获取管理员信息列表")
    @GetMapping("/get_managers")
    @ResponseBody
    public GlobalResult get_managers(@RequestParam(value = "query", required = false) String query,
                                     @RequestParam(value = "pagenum", required = false) String num,
                                     @RequestParam(value = "pagesize", required = false) String size) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<Manager> managers = this.adminManagerMapper.selectList(new QueryWrapper<Manager>().like("mg_name", query));
        if(managers==null){
            String message="用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }else {
            for (Manager manager : managers) {
                JSONObject item = new JSONObject();
                item.put("id", manager.getId());
                item.put("username", manager.getName());
                item.put("create_time", manager.getCreateTime());
                item.put("phone", manager.getPhone());
                item.put("email", manager.getEmail());
                Boolean state = "1".equals(manager.getState());
                item.put("state", state);
                array.add(item);
            }
            String message = "获取管理员列表成功";
            data.put("total", managers.size());
            data.put("pagenum", Integer.parseInt(num));
            data.put("users", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }

    @ApiOperation(value = "修改管理员状态")
    @PutMapping("/change_managerState/{id}/{state}")
    @ResponseBody
    public GlobalResult change_managerState(@PathVariable (value = "id") String id,
                                            @PathVariable (value = "state") Boolean state) {
        String newState = state ? "1" : "0";
        String message;
        Manager adminManager = new Manager();
        adminManager.setState(newState);
        adminManager.setId(id);
        int a = adminManagerMapper.updateById(adminManager);
        if (a == 1) {
            message = "用户状态修改成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "用户状态修改失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "添加管理员")
    @PostMapping("/add_manager")
    @ResponseBody
    public GlobalResult add_managers(@RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "password", required = false) String password,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "email", required = false) String email) {
        Manager adminManager = this.adminManagerMapper.selectOne(new QueryWrapper<Manager>().eq("mg_name", username));
        String message;
        if (adminManager == null) {
            Manager newManager = new Manager();
            newManager.setName(username);
            newManager.setPassWord(password);
            newManager.setCreateTime(String.valueOf(new Date().getTime()));
            newManager.setPhone(phone);
            newManager.setEmail(email);
            newManager.setState("0");
            int a = adminManagerMapper.insert(newManager);
            if(a==1) {
                JSONObject data=new JSONObject();
                data.put("id", newManager.getId());
                data.put("username", newManager.getName());
                data.put("create_time", newManager.getCreateTime());
                data.put("phone", newManager.getPhone());
                data.put("email", newManager.getEmail());
                Boolean state = "1".equals(newManager.getState());
                data.put("state", state);
                message = "成功添加新用户";
                GlobalResult result = GlobalResult.build(200, message, data);
                return result;
            }else{
                message = "添加失败";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }else {
            message = "该用户已存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "根据id获取管理人信息")
    @GetMapping("/get_managerById/{id}")
    @ResponseBody
    public GlobalResult get_managerById(@PathVariable(value = "id") String id){
        String message;
        Manager adminManager = this.adminManagerMapper.selectById(id);
        if(adminManager!=null){
            JSONObject data=new JSONObject();
            data.put("id", adminManager.getId());
            data.put("username", adminManager.getName());
            data.put("create_time", adminManager.getCreateTime());
            data.put("phone", adminManager.getPhone());
            data.put("email", adminManager.getEmail());
            message="获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else{
            message="获取失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "修改管理员信息")
    @PutMapping("/edit_manager/{id}")
    @ResponseBody
    public GlobalResult edit_manager(@PathVariable(value = "id", required = false) String id,
                                     @RequestBody Map<String,Object> reqMap){
        String message;
        Manager adminManager = this.adminManagerMapper.selectById(id);
        adminManager.setName(reqMap.get("username").toString());
        adminManager.setPhone(reqMap.get("phone").toString());
        adminManager.setEmail(reqMap.get("email").toString());
        int a = adminManagerMapper.updateById(adminManager);
        if (a == 1) {
            message = "用户信息修改成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "用户信息修改失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "删除管理员")
    @DeleteMapping("/delete_manager/{id}")
    @ResponseBody
    public GlobalResult delete_manager(@PathVariable(value = "id") String id){
        String message;
        int a = adminManagerMapper.deleteById(id);
        if(a==1){
            message="删除成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else{
            message="删除失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
}
