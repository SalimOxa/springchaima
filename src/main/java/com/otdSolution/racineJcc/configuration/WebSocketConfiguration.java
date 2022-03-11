package com.otdSolution.racineJcc.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;
import java.util.Queue;

@Configuration
@EnableWebSocketMessageBroker
//activé une messagerie soutenue par un courtier sur WebSocket à l'aide de STOMP
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // nous configurons un simple courtierQueue queue = new ActiveMQQueue("FOO.A,topic://NOTIFY.FOO.A");

        registry.enableSimpleBroker("/topic");
        // Set prefix for endpoints the client will send messages to
        registry.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
      //  nous avons enregistré les points de terminaison du stomp et active sockjs
        registry.addEndpoint("/sba-websocket").setAllowedOrigins("http://localhost:4200")
                .withSockJS()

        ;
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:4200")
                .withSockJS();


    }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration webSocketTransportRegistration) {

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration channelRegistration) {

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration channelRegistration) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> list) {
        return false;
    }


}
