package net.viancom.api_reservations.controller.resource;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import net.viancom.api_reservations.dto.ErrorDTO;
import net.viancom.api_reservations.dto.ReservationDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name="Reservation", description = "Operations related with reservation entity")
public interface ReservationResource {

    @Operation(description = "Get the information of all the reservations", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Return the information of all the reservations",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500",
                    description = "Something bad happens to obtain th reservations",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))),
    })
    public ResponseEntity<List<ReservationDTO>> getReservations();

    @Operation(description = "Get the information about one reservation", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Return the information of one reservation",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReservationDTO.class))),

            @ApiResponse(responseCode = "404",
                    description = "Reservation not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "500",
                    description = "Something bad happens to obtain th reservations",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "Id of the reservation to search", example = "1")
            }
    )
    public ResponseEntity<ReservationDTO> getReservationById(@Min(1) @PathVariable Long id);


    @Operation(description = "Create one reservation", responses = {
            @ApiResponse( responseCode = "201",
                    description = "Return the created reservation",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReservationDTO.class))
            )
    })
    public ResponseEntity<ReservationDTO> save(@RequestBody @Valid ReservationDTO reservation);
    public ResponseEntity<ReservationDTO> update(@Min(1) @PathVariable Long id, @RequestBody @Valid ReservationDTO reservation);
    public ResponseEntity<Void> delete(@Min(1) @PathVariable Long id);

}
