package net.viancom.api_reservations.connector;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import net.viancom.api_reservations.connector.configuration.EndpointConfiguration;
import net.viancom.api_reservations.connector.configuration.HostConfiguration;
import net.viancom.api_reservations.connector.configuration.HttpConnectorConfiguration;
import net.viancom.api_reservations.connector.response.CityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Component
public class CatalogConnector {
    private HttpConnectorConfiguration configuration;

    private final String HOST = "api-catalog";
    private final String ENDPOINT = "get-city";

    @Autowired
    public CatalogConnector(HttpConnectorConfiguration configuration) {
        this.configuration = configuration;
    }

    public CityDTO getCity(String code){

        HostConfiguration hostConfiguration = configuration.getHosts().get(HOST);
        EndpointConfiguration endpointConfiguration = hostConfiguration.getEndpoints().get(ENDPOINT);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(endpointConfiguration.getConnectionTimeout()))
                .doOnConnected(conn->conn
                        .addHandler(new ReadTimeoutHandler(endpointConfiguration.getConnectionTimeout(), TimeUnit.MILLISECONDS))
                        .addHandler(new WriteTimeoutHandler(endpointConfiguration.getWriteTimeout(), TimeUnit.MILLISECONDS))
                );

        WebClient client = WebClient.builder()
                .baseUrl("http://" + hostConfiguration.getHost()+ ":" + hostConfiguration.getPort() + endpointConfiguration.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        return client.get()
                .uri(uriBuilder -> uriBuilder.build(code))
                .retrieve()
                .bodyToMono(CityDTO.class)
                .share()
                .block();
    }
}
