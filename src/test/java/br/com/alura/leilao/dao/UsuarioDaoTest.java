package br.com.alura.leilao.dao;

import br.com.alura.leilao.builder.UsuarioBuilder;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static org.junit.jupiter.api.Assertions.fail;

public class UsuarioDaoTest {

    private UsuarioDao usuarioDao;
    private EntityManager em;
    private Usuario usuario;

    @BeforeEach
    public void iniciarAntesDeCadaTeste(){
        this.em = JPAUtil.getEntityManager();
        this.usuarioDao = new UsuarioDao(em);
        em.getTransaction().begin();
    }

    @AfterEach
    public void encerrarAposCadaTeste(){
        em.getTransaction().rollback();
    }

    @Test
    public void deveriaEncontrarUsuarioCadastrado (){

        Usuario usuario =  new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        Usuario usuarioEncontrado = this.usuarioDao.buscarPorUsername(usuario.getNome());
        Assert.assertNotNull(usuarioEncontrado);
    }

    @Test
    public void naoDeveriaEncontrarUsuarioNaoCadastrado (){
        Usuario usuario =  new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        Assert.assertThrows(NoResultException.class, () -> this.usuarioDao.buscarPorUsername("beltrano"));
    }

    @Test
    public void deveriaRemoverUmUsuario(){
        Usuario usuario =  new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        this.usuarioDao.deletar(usuario);
        Assert.assertThrows(NoResultException.class, () -> this.usuarioDao.buscarPorUsername("fulano"));

    }
}
