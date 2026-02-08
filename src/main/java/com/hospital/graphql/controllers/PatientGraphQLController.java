package com.hospital.config;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfig {

    private final PatientGraphQLResolver patientGraphQLResolver;

    public GraphQLConfig(PatientGraphQLResolver patientGraphQLResolver) {
        this.patientGraphQLResolver = patientGraphQLResolver;
    }

    @Bean
    public GraphQL graphQL() {
        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema()
                .query(patientGraphQLResolver)
                .mutation(patientGraphQLResolver)
                .build();
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
