package it.univaq.disim.connectorOTF.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@ComponentScan(basePackages="it.univaq.disim.connectorOTF.config")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements WebSocketMessageBrokerConfigurer{

	@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
                registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/");
                registry.addResourceHandler("/lib/**").addResourceLocations("/resources/lib/");
                registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/");
                registry.addResourceHandler("/templates/**").addResourceLocations("/resources/templates/");
                registry.addResourceHandler("/schema/**").addResourceLocations("/resources/schema/");
                registry.addResourceHandler("/img/**").addResourceLocations("/resources/img/");
                registry.addResourceHandler("/xml/**").addResourceLocations("/resources/xml/");
                registry.addResourceHandler("/json/**").addResourceLocations("/resources/json/");
	}

    @Override
    public void registerStompEndpoints(StompEndpointRegistry ser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration cr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration cr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry mbr) {        
        mbr.enableSimpleBroker("/log");
    }

	
}
