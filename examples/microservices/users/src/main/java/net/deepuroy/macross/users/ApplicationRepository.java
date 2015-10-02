package net.deepuroy.macross.users;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends
		PagingAndSortingRepository<Application, UUID> {

	Application findById(@Param("id") UUID id);

}
