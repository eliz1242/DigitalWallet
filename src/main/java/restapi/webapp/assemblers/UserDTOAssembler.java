package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import restapi.webapp.pojo.UserDTO;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserDTOAssembler implements SimpleRepresentationModelAssembler<UserDTO> {
    @Override
    public void addLinks(EntityModel<UserDTO> resource) {

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserDTO>> resources) {

    }

    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).userInfo(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).allUsersInfo()).withRel("back to all users DTO"));
    }

    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> entities) {
        List<EntityModel<UserDTO>> entityModels = new ArrayList<>();
        for (UserDTO entity : entities) {
            entityModels.add(toModel(entity));
        }
        return CollectionModel.of(entityModels,linkTo(methodOn(UserController.class)
                .allUsersInfo()).withSelfRel());
    }
}
