package org.hoboventures.personalFinance.dao;

import org.hoboventures.personalFinance.domain.LeaderDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Asha on 4/13/2017.
 */
@Repository
public interface LeaderRepository extends ElasticsearchRepository<LeaderDTO, Long> {
}
