package com.iwin;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.iwin.entity.User;
import com.iwin.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

/**
 * Unit test for simple App.
 */

@SpringBootTest(classes = App.class)
public class AppTest 
{

    @Resource
    private UserMapper userMapper;
    /**
     * Rigorous Test :-)
     */
    // 增加一条记录
    @Test
    public void shouldAnswerWithTrue()
    {
        User user = new User();
        user.setName("iwin");
        user.setAge(18);
        user.setEmail("12@qq.com");
        int row = userMapper.insert(user);

        System.out.println("影响记录数："+row);
        System.out.println("雪花算法id: "+user.getId());
    }

    // 根据id删除
    @Test
    public void deleteById() {
        int row = userMapper.deleteById(1429355285376094210L);
        System.out.println("影响记录数："+row);
    }

    // 构建map 根据map删除
    @Test
    public void updateUser() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "iwin");
       // map.put("age", 12);
        int row = userMapper.deleteByMap(map);
        System.out.println("影响记录数："+row);
    }

    // 根据主键查询一条数据
    @Test
    public void selectById() {
        User row = userMapper.selectById(1L);
        System.out.println("影响记录数："+row);
    }

    // 根据ids 批量查询
    @Test
    public void selectABatchByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<User> list = userMapper.selectBatchIds(ids);
        list.forEach(System.out::println);
    }

    // 指定查询结果字段
    @Test
    public void test01() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name", "age") // 指定查询结果字段
                .in("age", Arrays.asList(1,3))
                .last("limit 1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    // 根据自定义条件修改数据
    @Test
    public void test02() {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("name","iwin").eq("age", 18);

        User user = new User();
        user.setAge(18);
        user.setEmail("1111112@qq.com");
        int rows = userMapper.update(user, userUpdateWrapper);
        System.out.println("影响记录数：" + rows);
    }

    @Test
    public void testLike() {
        String name = "字母";  //name不为空
        String email = "";   //email为空串
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like(StringUtils.isNotEmpty(name), "name", name)
                //因为email为空串，该条件未生效
                .like(StringUtils.isNotEmpty(email), "email", email);

        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    @Test
    public void testAll() {
        //构造条件
        QueryWrapper<User> query = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "字母哥");
        params.put("age", 18);
        params.put("email", null);

       //query.allEq(params,false);
        query.allEq((k, v) -> !k.equals("name"), params, false);
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    @Test
    public void testLambda() {
      /*  LambdaQueryWrapper<User> lambdaQ = Wrappers.lambdaQuery();
        lambdaQ.like(User::getName, "字母")
                .lt(User::getAge, 18);
        List<User> list = userMapper.selectList(lambdaQ);*/

        List<User> list = new LambdaQueryChainWrapper<>(userMapper)
                .likeRight(User::getName, "字母")
                .and(q -> q.lt(User::getAge, 40)
                        .or()
                        .isNotNull(User::getEmail)
                )
                .list();
        list.forEach(System.out::println);
    }

    @Test
    public void testCustomSQL1() {
        String name = "字母";  //name不为空
        String email = "";   //email为空串
        List<User> list = userMapper.findUser(name,email);
        list.forEach(System.out::println);
    }

    @Test
    public void testCustomSQL2() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getName, "字母");

        List<User> list = userMapper.selectAll(query);
        list.forEach(System.out::println);
    }

    //////////////////////////////////////分页===============================
    @Test
    public void testSelect() {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.ge(User::getAge,10)      //查询条件：年龄大于10
                .orderByDesc(User::getAge);   //按照年龄的倒序排序

        Page<User> page = new Page<> (1,10,false);   //查询第1页，每页10条数据
        userMapper.selectPage(page,query);   //page分页信息，query查询条件

        System.out.println("总页数："+ page.getPages());
        System.out.println("总记录数："+ page.getTotal());

        // 分页返回的对象与传入的对象是同一个
        List<User> list = page.getRecords();
        list.forEach(System.out::println);
    }

   @Test
    public void startGenerator() {
        //1、全局配置
        GlobalConfig config = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        config.setActiveRecord(true)//开启AR模式
                .setAuthor("iwin")//设置作者
                .setOutputDir(projectPath + "/src/main/java")//生成路径(一般在此项目的src/main/java下)
                .setFileOverride(true)//第二次生成会把第一次生成的覆盖掉
                .setOpen(true)//生成完毕后是否自动打开输出目录
                //.setSwagger2(true)//实体属性 Swagger2 注解
                //.setIdType(IdType.AUTO)//主键策略
                .setServiceName("%sService")//生成的service接口名字首字母是否为I，这样设置就没有I
                .setBaseResultMap(true)//生成resultMap
                .setBaseColumnList(true);//在xml中生成基础列
        //2、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)//数据库类型
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://106.14.157.213:3306/springboot?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true")
                .setUsername("root")
                .setPassword("Dinghaiting19980501@");
        //3、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)//开启全局大写命名
                .setNaming(NamingStrategy.underline_to_camel)//表名映射到实体的命名策略(下划线到驼峰)
                //表字段映射属性名策略(未指定按naming)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                //.setTablePrefix("tb_")//表名前缀
                //.setSuperEntityClass("你自己的父类实体,没有就不用设置!")
                //.setSuperEntityColumns("id");//写于父类中的公共字段
                //.setSuperControllerClass("自定义继承的Controller类全称，带包名,没有就不用设置!")
                .setRestControllerStyle(true) //生成 @RestController 控制器
                .setEntityLombokModel(true)//使用lombok
                .setInclude("user");//逆向工程使用的表
        //4、包名策略配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.iwin")//设置包名的parent
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("entity")
                .setXml("mapper");//设置xml文件的目录
        //5、整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig);
        //6、执行
        autoGenerator.execute();
    }
}
