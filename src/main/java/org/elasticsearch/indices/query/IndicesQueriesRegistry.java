/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.indices.query;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryParser;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public class IndicesQueriesRegistry extends AbstractComponent {

    private ImmutableMap<String, QueryParser> queryParsers;

    @Inject
    public IndicesQueriesRegistry(Settings settings, Set<QueryParser> injectedQueryParsers) {
        super(settings);
        Map<String, QueryParser> queryParsers = Maps.newHashMap();
        for (QueryParser queryParser : injectedQueryParsers) {
            addQueryParser(queryParsers, queryParser);
        }
        this.queryParsers = ImmutableMap.copyOf(queryParsers);
    }

    /**
     * Adds a global query parser.
     */
    public synchronized void addQueryParser(QueryParser queryParser) {
        Map<String, QueryParser> queryParsers = Maps.newHashMap(this.queryParsers);
        addQueryParser(queryParsers, queryParser);
        this.queryParsers = ImmutableMap.copyOf(queryParsers);
    }

    public ImmutableMap<String, QueryParser> queryParsers() {
        return queryParsers;
    }

    private void addQueryParser(Map<String, QueryParser> queryParsers, QueryParser queryParser) {
        for (String name : queryParser.names()) {
            queryParsers.put(name, queryParser);
        }
    }
}