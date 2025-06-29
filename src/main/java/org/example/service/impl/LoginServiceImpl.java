package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.Admin;
import org.example.entity.Sysadmin;
import org.example.entity.User;
import org.example.form.LoginForm;
import org.example.mapper.AdminMapper;
import org.example.mapper.SysadminMapper;
import org.example.mapper.UserMapper;
import org.example.result.LoginResult;
import org.example.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired  //自动注入对象
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private SysadminMapper sysadminMapper;
    @Override
    public LoginResult login(LoginForm loginForm) {
        LoginResult loginResult = new LoginResult();
        //三种角色，使用switch分支区别
        switch (loginForm.getType()){
            case 1:
                //先查用户名，再检测密码
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", loginForm.getUsername());
                User user = this.userMapper.selectOne(queryWrapper);
                if(user == null) {
                    loginResult.setMsg("用户名不存在");
                    loginResult.setCode(-1);
                    return loginResult;
                }
                //验证密码
                if(!user.getPassword().equals(loginForm.getPassword())){
                    loginResult.setMsg("密码错误");
                    loginResult.setCode(-2);
                    return loginResult;
                }
                loginResult.setMsg("登录成功");
                loginResult.setCode(0);
                loginResult.setObject(user);
                break;
            case 2:
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("username", loginForm.getUsername());
                Admin admin = this.adminMapper.selectOne(adminQueryWrapper);
                if(admin == null) {
                    loginResult.setMsg("用户名不存在");
                    loginResult.setCode(-1);
                    return loginResult;
                }
                //验证密码
                if(!admin.getPassword().equals(loginForm.getPassword())){
                    loginResult.setMsg("密码错误");
                    loginResult.setCode(-2);
                    return loginResult;
                }
                loginResult.setMsg("登录成功");
                loginResult.setCode(0);
                loginResult.setObject(admin);
                break;
            case 3:
                QueryWrapper<Sysadmin> sysadminQueryWrapper = new QueryWrapper<>();
                sysadminQueryWrapper.eq("username", loginForm.getUsername());
                Sysadmin sysadmin = this.sysadminMapper.selectOne(sysadminQueryWrapper);
                if(sysadmin == null) {
                    loginResult.setMsg("用户名不存在");
                    loginResult.setCode(-1);
                    return loginResult;
                }
                //验证密码
                if(!sysadmin.getPassword().equals(loginForm.getPassword())){
                    loginResult.setMsg("密码错误");
                    loginResult.setCode(-2);
                    return loginResult;
                }
                loginResult.setMsg("登录成功");
                loginResult.setCode(0);
                loginResult.setObject(sysadmin);

                break;

        }
        return loginResult;
    }
}
