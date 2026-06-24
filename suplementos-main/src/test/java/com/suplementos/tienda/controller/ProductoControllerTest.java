package com.suplementos.tienda.controller;

import com.suplementos.tienda.model.Producto;
import com.suplementos.tienda.repository.ProductoRepository;
import com.suplementos.tienda.security.JwtAuthFilter;
import com.suplementos.tienda.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@WithMockUser
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository productoRepo;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void listar_deberiaRetornarListaDeProductos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Proteína Whey");

        when(productoRepo.findAll()).thenReturn(List.of(producto));

        Mockito.doAnswer(invocation -> {
    ServletRequest request = invocation.getArgument(0);
    ServletResponse response = invocation.getArgument(1);
    FilterChain chain = invocation.getArgument(2);
    chain.doFilter(request, response);
    return null;
}).when(jwtAuthFilter).doFilter(Mockito.any(), Mockito.any(), Mockito.any());

        mockMvc.perform(get("/api/productos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Proteína Whey"));
    }
}