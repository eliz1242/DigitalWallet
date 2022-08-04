package restapi.webapp.assemblers;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import restapi.webapp.controllers.WalletController;
import restapi.webapp.pojo.User;
import restapi.webapp.pojo.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class UserAssembler extends SimpleIdentifiableRepresentationModelAssembler<User> {
    public UserAssembler() {
        super(UserController.class);
    }

    @Override
    public EntityModel<User> toModel(User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserController.class).getSingleProfile(entity.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllProfiles()).withRel("back to all users"));
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        List<EntityModel<User>> entityModels = new ArrayList<>();
        for (User entity : entities) {
            entityModels.add(toModel(entity));
        }
        return CollectionModel.of(entityModels,linkTo(methodOn(UserController.class)
                .getAllProfiles()).withSelfRel());
    }
}
