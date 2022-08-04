package restapi.webapp.assemblers;



import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.WalletController;
import restapi.webapp.pojo.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class WalletAssembler extends SimpleIdentifiableRepresentationModelAssembler<Wallet> {
    public WalletAssembler() {
        super(WalletController.class);
    }/**/

    @Override
    public EntityModel<Wallet> toModel(Wallet entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(WalletController.class).getSingleWallet(entity.getId())).withSelfRel(),
                linkTo(methodOn(WalletController.class).getAllWallets()).withRel("back to all wallets"));
    }

    @Override
    public CollectionModel<EntityModel<Wallet>> toCollectionModel(Iterable<? extends Wallet> entities) {
        List<EntityModel<Wallet>> entityModels = new ArrayList<>();
        for (Wallet entity : entities) {
            entityModels.add(toModel(entity));
        }
        return CollectionModel.of(entityModels,linkTo(methodOn(WalletController.class)
                .getAllWallets()).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Wallet>> resources) {
        super.addLinks(resources);
        resources.add(linkTo(methodOn(WalletController.class).getAllWallets()).withRel("wallets"));
    }
}