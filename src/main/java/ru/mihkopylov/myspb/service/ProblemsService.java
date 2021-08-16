package ru.mihkopylov.myspb.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ru.mihkopylov.myspb.Api;
import ru.mihkopylov.myspb.exception.NoReasonInReasonGroupException;
import ru.mihkopylov.myspb.exception.PositionTypeNotFoundException;
import ru.mihkopylov.myspb.exception.ReasonGroupNotFoundException;
import ru.mihkopylov.myspb.exception.UserNotFoundException;
import ru.mihkopylov.myspb.interceptor.SessionContext;
import ru.mihkopylov.myspb.model.ReasonGroup;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.dto.*;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class ProblemsService {
    @NonNull
    private final ReasonGroupService reasonGroupService;
    @NonNull
    private final ReasonService reasonService;
    @NonNull
    private final HttpService httpService;
    @NonNull
    private final SessionContext sessionContext;
    @NonNull
    private final MapsService mapsService;

    @NonNull
    public List<ShortProblemResponse> getProblems() {
        return httpService.get(Api.MY_PROBLEMS, ProblemsPageResponse.class).getResults();
    }

    @NonNull
    public ProblemResponse getProblem(@NonNull Long id) {
        return httpService.get(Api.PROBLEMS + id, ProblemResponse.class);
    }

    @NonNull
    public ProblemResponse createProblem(@NonNull Long reasonGroupId, @NonNull Double latitude,
                                         @NonNull Double longitude, @NonNull MultipartFile[] files, @NonNull String body) {
        User user = sessionContext.getUser().orElseThrow(UserNotFoundException::new);
        ReasonGroup reasonGroup = reasonGroupService.findByUserAndId(user, reasonGroupId)
                .orElseThrow(ReasonGroupNotFoundException::new);
        if (isNull(reasonGroup.getReasonId())) {
            throw new NoReasonInReasonGroupException();
        }
        PositionType positionType = reasonService.getReasons()
                .stream()
                .map(CityObjectResponse::getCategories)
                .flatMap(Collection::stream)
                .map(CategoryResponse::getReasons)
                .flatMap(Collection::stream)
                .filter(o -> o.getId().equals(reasonGroup.getReasonId()))
                .findAny()
                .map(ReasonResponse::getPositionType)
                .orElseThrow(NoReasonInReasonGroupException::new);

        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("body", body);
        request.add("reason", reasonGroup.getReasonId());
        switch (positionType) {
            case BUILDING:
                BuildingResponse nearestBuilding = mapsService.getNearestBuilding(latitude, longitude);
                request.add("latitude", nearestBuilding.getLatitude());
                request.add("longitude", nearestBuilding.getLongitude());
                request.add("building", nearestBuilding.getId());
                break;
            case STREET:
                request.add("latitude", latitude);
                request.add("longitude", longitude);
                break;
            case NEAR_BUILDING:
                request.add("latitude", latitude);
                request.add("longitude", longitude);
                request.add("nearest_building", mapsService.getNearestBuilding(latitude, longitude).getId());
                break;
            default:
                throw new PositionTypeNotFoundException();
        }
        for (MultipartFile file : files) {
            request.add("files", file.getResource());
        }
        CreatedProblemResponse response = httpService.post(Api.PROBLEMS, request, CreatedProblemResponse.class);
        return getProblem(response.getId());
    }
}
