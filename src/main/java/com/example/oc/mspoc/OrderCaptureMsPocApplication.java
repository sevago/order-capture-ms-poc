package com.example.oc.mspoc;

import java.util.Arrays;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.oc.mspoc.controller.OrderCaptureRestController;
import com.example.oc.mspoc.listener.AssignedProductMessageListener;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = JmxAutoConfiguration.class)
@EnableAutoConfiguration
public class OrderCaptureMsPocApplication {
	
	public final static String OC_AP_MESSAGE_QUEUE = "oc-ap-message-queue";
	public final static String OC_OS_MESSAGE_QUEUE = "oc-os-message-queue";

	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
	  final RedisTemplate<String, Object> template =  new RedisTemplate<String, Object>();
	  template.setConnectionFactory(connectionFactory);
	  template.setKeySerializer(new StringRedisSerializer());
	  template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
	  template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
	  return template;
	}

	@Bean
	Queue queue() {
		return new Queue(OC_AP_MESSAGE_QUEUE, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(OC_AP_MESSAGE_QUEUE);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
	MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(OC_AP_MESSAGE_QUEUE);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(AssignedProductMessageListener receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(OrderCaptureMsPocApplication.class, args);
		//logAppBeans(ctx);		
		//OrderCaptureRestController controller = (OrderCaptureRestController) ctx.getBean("orderCaptureRestController");
		//controller.getOrderSummary("520665318A", true);	
	}
	
	@SuppressWarnings("unused")
	private static void logAppBeans(ApplicationContext ctx) {
		String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
	}
}
