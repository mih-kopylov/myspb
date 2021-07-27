package ru.mihkopylov.myspb.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.mihkopylov.myspb.Const;
import ru.mihkopylov.myspb.aspect.RefreshTokenIfRequired;
import ru.mihkopylov.myspb.controller.dto.CreateReasonGroupRequest;
import ru.mihkopylov.myspb.controller.dto.ImportReqsonGroupsRequest;
import ru.mihkopylov.myspb.controller.dto.ReasonGroupResponse;
import ru.mihkopylov.myspb.exception.ReasonGroupNotFoundException;
import ru.mihkopylov.myspb.exception.UserNotFoundException;
import ru.mihkopylov.myspb.exception.UserToImportNotFoundException;
import ru.mihkopylov.myspb.interceptor.RequestContext;
import ru.mihkopylov.myspb.model.ReasonGroup;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.ReasonGroupService;
import ru.mihkopylov.myspb.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(Const.REST + "/reasonGroups")
@AllArgsConstructor
@Slf4j
public class ReasonGroupController {
    @NonNull
    private final ReasonGroupService reasonGroupService;
    @NonNull
    private final RequestContext requestContext;
    @NonNull
    private final UserService userService;

    @GetMapping
    @RefreshTokenIfRequired
    public List<ReasonGroupResponse> getGroups() {
        log.debug("get reason groups");
        User user = requestContext.getUser().orElseThrow(UserNotFoundException::new);
        return reasonGroupService.findByUser(user).stream().map(ReasonGroupResponse::new).collect(toList());
    }

    @GetMapping("/{id}")
    @RefreshTokenIfRequired
    public ReasonGroupResponse getGroup(@PathVariable("id") Long id) {
        log.debug("get reason group");
        User user = requestContext.getUser().orElseThrow(UserNotFoundException::new);
        return reasonGroupService.findByUserAndId(user, id)
                .map(ReasonGroupResponse::new)
                .orElseThrow(ReasonGroupNotFoundException::new);
    }

    @PostMapping
    @RefreshTokenIfRequired
    public ReasonGroupResponse createGroup(@RequestBody @Valid CreateReasonGroupRequest request) {
        log.debug("create reason group");
        User user = requestContext.getUser().orElseThrow(UserNotFoundException::new);
        ReasonGroup reasonGroup =
                reasonGroupService.createGroup(user, request.getName(), request.getParentId(), request.getReasonId(),
                        request.getBody());
        return new ReasonGroupResponse(reasonGroup);
    }

    @PutMapping("/{id}")
    @RefreshTokenIfRequired
    public ReasonGroupResponse updateGroup(@PathVariable("id") Long id,
                                           @RequestBody @Valid CreateReasonGroupRequest request) {
        log.debug("update reason group");
        User user = requestContext.getUser().orElseThrow(UserNotFoundException::new);
        ReasonGroup reasonGroup =
                reasonGroupService.findByUserAndId(user, id).orElseThrow(ReasonGroupNotFoundException::new);
        return new ReasonGroupResponse(
                reasonGroupService.updateReasonGroup(reasonGroup, request.getName(), request.getParentId(),
                        request.getReasonId(), request.getBody()));
    }

    @DeleteMapping("/{id}")
    @RefreshTokenIfRequired
    public void deleteGroup(@PathVariable("id") Long id) {
        log.debug("delete reason group");
        User user = requestContext.getUser().orElseThrow(UserNotFoundException::new);
        ReasonGroup reasonGroup =
                reasonGroupService.findByUserAndId(user, id).orElseThrow(ReasonGroupNotFoundException::new);
        reasonGroupService.deleteReasonGroup(reasonGroup);
    }

    @PostMapping("/import")
    @RefreshTokenIfRequired
    public void importGroups(@RequestBody @Valid ImportReqsonGroupsRequest request) {
        log.debug("import reason groups");
        User user = requestContext.getUser().orElseThrow(UserNotFoundException::new);
        User userToImport =
                userService.findUserByLogin(request.getLogin()).orElseThrow(UserToImportNotFoundException::new);

        reasonGroupService.importFromUser(user, userToImport);
    }
}
