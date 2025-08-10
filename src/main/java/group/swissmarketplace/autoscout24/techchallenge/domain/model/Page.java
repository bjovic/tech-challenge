package group.swissmarketplace.autoscout24.techchallenge.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record Page<T>(
    PageInformation page,
    long total,
    List<T> content
) {

}

