package restapi.webapp.assemblers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import restapi.webapp.controllers.CoinController;
import restapi.webapp.pojo.CoinDTO;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CoinDTOAssembler implements SimpleRepresentationModelAssembler<CoinDTO> {

    @Override
    public EntityModel<CoinDTO> toModel(CoinDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CoinController.class).getCoinDTOInfo(entity.getId())).withSelfRel(),
                linkTo(methodOn(CoinController.class).getAllCoinsDTOInfo()).withRel("back to all users DTO"));
    }

    @Override
    public CollectionModel<EntityModel<CoinDTO>> toCollectionModel(Iterable<? extends CoinDTO> entities) {
        List<EntityModel<CoinDTO>> entityModels = new ArrayList<>();
        for (CoinDTO entity : entities) {
            entityModels.add(toModel(entity));
        }
        return CollectionModel.of(entityModels,linkTo(methodOn(CoinController.class)
                .getAllCoinsDTOInfo()).withSelfRel());
    }

    @Override
    public void addLinks(EntityModel<CoinDTO> resource) {

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<CoinDTO>> resources) {

    }
}
