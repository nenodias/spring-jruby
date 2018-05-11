package io.github.nenodias.springjruby.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ruby")
public class RubyController {
    
    private static final Logger LOGGER = Logger.getLogger(RubyController.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndexPage() {
        ScriptingContainer ruby = new ScriptingContainer(LocalContextScope.THREADSAFE, LocalVariableBehavior.PERSISTENT);
        ruby.addClassLoader(getClass().getClassLoader());
        try{
            ruby.put("jdbc", jdbcTemplate);
            List lista = jdbcTemplate.query("SELECT * FROM USERS ", (rs, rowNum) ->{
                Map<String, Object>mapa = new HashMap<>();
                mapa.put("id", rs.getInt("ID"));
                mapa.put("nome", rs.getString("NOME"));
                mapa.put("idade", rs.getInt("IDADE"));
                return mapa;
            });
            Object retorno = ruby.runScriptlet(PathType.CLASSPATH, "ruby/info.rb");
            System.out.println(retorno);
            System.out.println(lista);
        }catch(Exception ex){
            ex.printStackTrace();
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ModelAndView("info");
    }

}