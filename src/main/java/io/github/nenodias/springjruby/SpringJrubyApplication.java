package io.github.nenodias.springjruby;

import org.apache.log4j.Logger;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SpringJrubyApplication {

    private static final Logger LOGGER = Logger.getLogger(SpringJrubyApplication.class);
    
    public static void main(String[] args) {
        SpringApplication.run(SpringJrubyApplication.class, args);
    }
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Bean
    public ScriptingContainer rubyContainer(){
        ScriptingContainer ruby = new ScriptingContainer(LocalContextScope.THREADSAFE, LocalVariableBehavior.PERSISTENT);
        ruby.addClassLoader(applicationContext.getClassLoader());
        return ruby;
    }
    
    @Bean
    public SmartInitializingSingleton importProcessor() {
        return () -> {
            final JdbcTemplate jdbc = applicationContext.getBean(JdbcTemplate.class);
            try{
                jdbc.execute("SELECT 1 FROM users");
            }catch(Exception ex){
                LOGGER.error(ex.getMessage());
                jdbc.execute("CREATE TABLE users(id int auto_increment primary key, name varchar(200) not null, email varchar(200) not null, password varchar(200) not null, idade int)");
                jdbc.execute("INSERT INTO users(name, email, password, idade) VALUES('Horácio','horacio@gmail.com','123', 25) ");
            }
            System.out.println("Started");
        };
    }

}
