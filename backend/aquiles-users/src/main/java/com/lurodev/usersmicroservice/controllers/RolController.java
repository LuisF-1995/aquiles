package com.lurodev.usersmicroservice.controllers;

import com.lurodev.usersmicroservice.models.dto.RequestResponse;
import com.lurodev.usersmicroservice.models.Rol;
import com.lurodev.usersmicroservice.services.IRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rol")
@RequiredArgsConstructor
public class RolController {
    private final IRolService rolService;

    @GetMapping
    public ResponseEntity<RequestResponse> getAllRoles(){
        List<Rol> roles = rolService.getAllRoles();
        RequestResponse response = new RequestResponse(roles, true, HttpStatus.OK.value(), "All Roles");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponse> getRolById(@PathVariable("id") Long id){
        Rol rol = rolService.getRolById(id);
        RequestResponse response = new RequestResponse(null, false, HttpStatus.NOT_FOUND.value(), "Rol not found");

        if(rol != null){
            response = new RequestResponse(rol, true, HttpStatus.OK.value(), "Rol by id " + id);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol){
        return ResponseEntity.ok(rolService.createRol(rol));
    }

    @PutMapping
    public ResponseEntity<Rol> updateRol(@RequestBody Rol rol){
        return ResponseEntity.ok(rolService.createRol(rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRolById(@PathVariable("id") Long id){
        return ResponseEntity.ok(rolService.deleteRolById(id));
    }
}
