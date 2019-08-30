package com.yuanian.test;

import com.yuanian.bean.Department;
import com.yuanian.bean.Employee;
import com.yuanian.dao.DepartmentMapper;
import com.yuanian.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * 测试dao层的工作
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlSession;



   @Test
    public void testCRUD() throws Exception{
//        //1、创建SpringIOC容器
//        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
//        //2、从容器中获取mapper
//        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);
//       departmentMapper.insert(new Department(,"ss"));
//       departmentMapper.insertSelective(new Department(3,"jisuan"));
//       departmentMapper.insertSelective(new Department(null,"shen"));
//        System.out.println(departmentMapper);
       EmployeeMapper mapper=sqlSession.getMapper(EmployeeMapper.class);
       for(int i=0;i<1000;i++) {
           String uid = UUID.randomUUID().toString().substring(0,5)+i;
           mapper.insertSelective(new Employee(null,uid,"M",uid+"@yuanian.com",1));
       }
       System.out.println("批量完成");
    }
}
