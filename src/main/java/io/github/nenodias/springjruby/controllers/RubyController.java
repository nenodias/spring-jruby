package io.github.nenodias.springjruby.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jruby.RubyHash;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
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
    
    @Autowired
    private ScriptingContainer ruby;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndexPage() {
        try{
            ruby.put("jdbc", jdbcTemplate);
            List lista = jdbcTemplate.query("SELECT * FROM users ", (rs, rowNum) ->{
                Map<String, Object>mapa = new HashMap<>();
                mapa.put("id", rs.getInt("ID"));
                mapa.put("name", rs.getString("NAME"));
                mapa.put("email", rs.getString("EMAIL"));
                return mapa;
            });
            //Object retorno = ruby.runScriptlet(PathType.CLASSPATH, "ruby/info.rb");
            List<Map<String, Object>> retorno = (List) ruby.runScriptlet(getClass().getResourceAsStream("/ruby/info.rb"), "info.rb");
            Map<String, Object> hash = (RubyHash)retorno.get(0);
            System.out.println(hash.keySet());
            System.out.println(hash);
            System.out.println(retorno.get(0).getClass());
            //System.out.println(lista);
        }catch(Exception ex){
            ex.printStackTrace();
            LOGGER.error(ex.getMessage(), ex);
        }
        return new ModelAndView("info");
    }

}