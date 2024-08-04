package com.mindzone.dto.request;

import com.mindzone.enums.*;
import com.mindzone.model.user.WeekDayAvailability;
import com.mindzone.model.user.filter.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchFilter implements Serializable {

    private List<Profession> professions;
    private List<Gender> genders;
    private List<SessionModality> sessionModalities;
    private List<PaymentMethod> paymentMethods;
    private List<Approach> approaches;
    private List<Speciality> specialities;
    private List<HealthPlan> acceptedHealthPlans;

    private List<WeekDayAvailability> availability;
    private AvailabilitySearchType availabilitySearchType;

    private String name;
    private String professionalCode;
    private String neighborhood;
    private String city;
    private State state;

    private BigDecimal minimumPrice;
    private BigDecimal maximumPrice;
    private Integer minimumAge;
    private Integer maximumAge;

    private Integer page;
    private Integer size;

    public List<Filter> getList() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new ProfessionsFilter(professions));
        filters.add(new GendersFilter(genders));
        filters.add(new SessionModalitiesFilter(sessionModalities));
        filters.add(new PaymentMethodsFilter(paymentMethods));
        filters.add(new ApproachesFilter(approaches));
        filters.add(new AcceptedHealthPlansFilter(acceptedHealthPlans));
        filters.add(new NameFilter(name));
        filters.add(new ProfessionalCodeFilter(professionalCode));
        filters.add(new NeighborhoodFilter(neighborhood));
        filters.add(new CityFilter(city));
        filters.add(new StateFilter(state));
        filters.add(new AvailabilityFilter(availability, availabilitySearchType));
        filters.add(new AgeFilter(minimumAge, maximumAge));
        filters.add(new PriceFilter(minimumPrice, maximumPrice));

        return filters;
    }
}
