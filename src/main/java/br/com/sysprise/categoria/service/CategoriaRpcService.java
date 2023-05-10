package br.com.sysprise.categoria.service;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pb.*;

import java.util.List;


@GrpcService
public class CategoriaRpcService extends pb.CategoriaServiceGrpc.CategoriaServiceImplBase {

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public void createCategoria(CriarCategoriaRequest request, StreamObserver<CategoriaId> responseObserver) {
        Long categoriaId = categoriaService.cadastrar(request);
        CategoriaId categoriaIdResponse = CategoriaId.newBuilder().setId(categoriaId).build();
        responseObserver.onNext(categoriaIdResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getCategoria(CategoriaId request, StreamObserver<Categoria> responseObserver) {
        br.com.sysprise.categoria.model.Categoria categoria = categoriaService.findCategoriaById(request.getId());
        Categoria categoriaResponse = Categoria.newBuilder().setId(categoria.getId()).setNome(categoria.getNome()).setDescricao(categoria.getDescricao()).build();
        responseObserver.onNext(categoriaResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void listCategorias(blank request, StreamObserver<pb.ListaCategorias> responseObserver) {
        List<Categoria> categorias =
                categoriaService.listar().stream()
                        .map(categoria ->
                            Categoria.newBuilder()
                                    .setId(categoria.getId())
                                    .setNome(categoria.getNome())
                                    .setDescricao(categoria.getDescricao())
                                    .build())
                        .toList();
        ListaCategorias listaCategorias = ListaCategorias.newBuilder().addAllCategoria(categorias).build();
        responseObserver.onNext(listaCategorias);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCategoria(CategoriaId request, StreamObserver<blank> responseObserver) {
        categoriaService.deletar(request.getId());
        responseObserver.onCompleted();
    }
}
