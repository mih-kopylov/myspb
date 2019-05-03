package ru.mihkopylov.myspb.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.mihkopylov.myspb.dao.ReasonGroupDao;
import ru.mihkopylov.myspb.model.ReasonGroup;
import ru.mihkopylov.myspb.exception.ReasonGroupNotFoundException;
import ru.mihkopylov.myspb.exception.ReasonNotFoundException;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.dto.CategoryResponse;
import ru.mihkopylov.myspb.service.dto.CityObjectResponse;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class ReasonGroupService {
    @NonNull
    private final ReasonGroupDao reasonGroupDao;
    @NonNull
    private final ReasonService reasonService;

    @NonNull
    public List<ReasonGroup> findByUser( @NonNull User user ) {
        return reasonGroupDao.findByUser( user );
    }

    @NonNull
    public ReasonGroup createGroup( @NonNull User user, @NonNull String name, @Nullable Long parentId,
            @Nullable Long reasonId, @Nullable String body ) {
        validateReasonId( reasonId );
        ReasonGroup parent = isNull( parentId ) ? null
                : reasonGroupDao.findById( parentId ).orElseThrow( ReasonGroupNotFoundException :: new );
        return reasonGroupDao.create( user, name, parent, reasonId, body );
    }

    @NonNull
    public Optional<ReasonGroup> findByUserAndId( @NonNull User user, @NonNull Long id ) {
        return reasonGroupDao.findByUserAndId( user, id );
    }

    @NonNull
    public ReasonGroup updateReasonGroup( @NonNull ReasonGroup reasonGroup, @NonNull String name,
            @Nullable Long parentId, @Nullable Long reasonId, @Nullable String body ) {
        validateReasonId( reasonId );
        ReasonGroup parent = isNull( parentId ) ? null
                : reasonGroupDao.findById( parentId ).orElseThrow( ReasonGroupNotFoundException :: new );
        reasonGroup.setName( name );
        reasonGroup.setParent( parent );
        reasonGroup.setReasonId( reasonId );
        reasonGroup.setBody( body );
        return reasonGroupDao.save( reasonGroup );
    }

    public void deleteReasonGroup( @NonNull ReasonGroup reasonGroup ) {
        reasonGroupDao.delete( reasonGroup );
    }

    private void validateReasonId( @Nullable Long reasonId ) {
        if (nonNull( reasonId )) {
            reasonService.getReasons()
                    .stream()
                    .map( CityObjectResponse :: getCategories )
                    .flatMap( Collection :: stream )
                    .map( CategoryResponse :: getReasons )
                    .flatMap( Collection :: stream )
                    .filter( o -> o.getId().equals( reasonId ) )
                    .findAny()
                    .orElseThrow( ReasonNotFoundException :: new );
        }
    }
}