package com.yuanian.controller;
import	java.util.ArrayList;
import	java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuanian.bean.Employee;
import com.yuanian.bean.Msg;
import com.yuanian.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;



@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    /**
     * 删除操作
     * @param ids
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmpById(@PathVariable("id")String ids){
        //批量删除
        if (ids != null){
            List<Integer> del_ids = new ArrayList<> ();
            String[] str_ids = ids.split("-");
            //组装id集合
            for (String string : str_ids){
                del_ids.add(Integer.parseInt(string));
            }
//            for (String id : str_ids)
            employeeService.deleteBatch(del_ids);
            return Msg.success();
        }else {
            return Msg.fail();
        }


//        else {
//            Integer id = Integer.parseInt(ids);
//            employeeService.deleteEmp(id);
//        }

    }


    /**
     * 员工更新方法
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
    public Msg saveEmp(Employee employee,HttpServletRequest request){
//        System.out.println("请求体中的值："+request.getParameter("gender"));
//        System.out.println("将要更新的员工数据："+employee);
        employeeService.updateEmp(employee);
        return Msg.success()	;
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    /**
     * 检查用户名是否可用，后端校验
     * @param empName
     * @return
     */
    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkuser(String empName) {
        //先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
        if (!empName.matches(regx)) {
            return Msg.fail().add("va_msg", "用户名必须是6-16位数字和字母的组合或者2-5位中文");
        }

        //数据库用户名重复校验
        boolean flag;
                flag= employeeService.checkUser(empName);
        if (flag) {
            return Msg.success();
        } else {
            return Msg.fail().add("va_msg","用户名不可用");
        }
    }

    /**
     * 员工保存
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()){
            //校验失败，应该返回失败，在模态框中显示校验失败的错误信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError:errors){
                System.out.println();
            }
            return Msg.fail().add("errorFields",map);
        }else {
            employeeService.saveEmp(employee);
//            List<Employee> all = employeeService.getAll();
//            return Msg.success().add("extend", all);

            List<Employee> emps = employeeService.getAll();
            //使用PageInfo包装查询后的结果，只需要将pageInfo交给页面就行
            //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
            PageInfo page = new PageInfo(emps,6);
            return Msg.success().add("pageInfo", page);
        }
    }


    /**
     * 查询所有信息
     * @param pn：页码
     * @param model
     * @return返回Json字符串
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(
            @RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        //这不是一个分页查询;
        //引入PageHelper分页查询
        // 在使用之前只需要调用,传入页码，以及每页的大小
        PageHelper.startPage(pn, 8);
        //startPage后面紧跟的这个查询就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交给页面就行
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps,6);
        return Msg.success().add("pageInfo", page);
    }

    /**
     * 查询员工数据
     * @return
     */
//    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        //这不是一个分页查询;
        //引入PageHelper分页查询
        // 在使用之前只需要调用,传入页码，以及每页的大小
        PageHelper.startPage(pn, 8);
        //startPage后面紧跟的这个查询就是一个分页查询
        List<Employee> all = employeeService.getAll();
        //使用PageInfo包装查询后的结果，只需要将pageInfo交给页面就行
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
//        PageInfo page = new PageInfo(emps);
//        model.addAttribute("pageInfo", page);
//        page.getNavigatepageNums();
//        System.out.println(page.getNavigatepageNums());
        return "list";
    }
}
