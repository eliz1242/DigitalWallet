package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.UserController;
import restapi.webapp.controllers.WalletController;
import restapi.webapp.pojo.Wallet;
import restapi.webapp.pojo.WalletDTO;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class WalletDTOAssembler implements SimpleRepresentationModelAssembler<WalletDTO> {
    @Override
    public EntityModel<WalletDTO> toModel(WalletDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(WalletController.class).walletInfo(entity.getId())).withSelfRel(),
                linkTo(methodOn(WalletController.class).allWalletsInfo()).withRel("back to all wallets DTO"));
    }

    @Override
    public CollectionModel<EntityModel<WalletDTO>> toCollectionModel(Iterable<? extends WalletDTO> entities) {
        List<EntityModel<WalletDTO>> entityModels = new ArrayList<>();
        for (WalletDTO entity : entities) {
            entityModels.add(toModel(entity));
        }
        return CollectionModel.of(entityModels,linkTo(methodOn(WalletController.class)
                .allWalletsInfo()).withSelfRel());
    }

    @Override
    public void addLinks(EntityModel<WalletDTO> resource) {
        resource.add(
                WebMvcLinkBuilder.linkTo(methodOn(WalletController.class).walletInfo(resource.getContent().getWallet().getId())).withSelfRel());
        //resource.add(linkTo(methodOn(WalletController.class).getSingleWallet(resource.getContent().getWallet().getId())).withRel("single wallet"));
        resource.add(linkTo(methodOn(WalletController.class).allWalletsInfo()).withRel("back to all wallets DTO"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<WalletDTO>> resources) {
        resources.add(linkTo(methodOn(WalletController.class).allWalletsInfo()).withRel("wallets"));
    }
}
