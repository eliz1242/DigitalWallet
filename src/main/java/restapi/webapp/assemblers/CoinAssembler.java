package restapi.webapp.assemblers;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.CoinController;
import restapi.webapp.pojo.Coin;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class CoinAssembler extends SimpleIdentifiableRepresentationModelAssembler<Coin> {
    public CoinAssembler() {
        super(CoinController.class);
    }/**/

    @Override
    public EntityModel<Coin> toModel(Coin entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CoinController.class).getSingleCoin(entity.getId())).withSelfRel(),
                linkTo(methodOn(CoinController.class).getAllCoins()).withRel("back to all coins"));
    }

    @Override
    public CollectionModel<EntityModel<Coin>> toCollectionModel(Iterable<? extends Coin> entities) {
        List<EntityModel<Coin>> entityModels = new ArrayList<>();
        for (Coin entity : entities) {
            entityModels.add(toModel(entity));
        }
        return CollectionModel.of(entityModels,linkTo(methodOn(CoinController.class)
                .getAllCoins()).withSelfRel());
    }
}
