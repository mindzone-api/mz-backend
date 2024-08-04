package com.mindzone.model.user.filter;

import org.springframework.data.mongodb.core.query.Query;
public interface Filter {

    void apply(Query query);
}
