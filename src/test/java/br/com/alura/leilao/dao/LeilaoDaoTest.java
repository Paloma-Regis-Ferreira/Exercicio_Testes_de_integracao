package br.com.alura.leilao.dao;

import br.com.alura.leilao.builder.LeilaoBuilder;
import br.com.alura.leilao.builder.UsuarioBuilder;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LeilaoDaoTest {

    private LeilaoDao leilaoDao;
    private EntityManager em;
    private Usuario usuario;

    @BeforeEach
    public void iniciarAntesDeCadaTeste(){
        this.em = JPAUtil.getEntityManager();
        this.leilaoDao = new LeilaoDao(em);
        em.getTransaction().begin();
    }

    @AfterEach
    public void encerrarAposCadaTeste(){
        em.getTransaction().rollback();
    }

    @Test
    public void deveriaCadastrarUmLeilao(){
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("500")
                .comData(LocalDate.now())
                .comUsuario(usuario)
                .criar();


        leilao = leilaoDao.salvar(leilao);

        Leilao salvo = leilaoDao.buscarPorId(leilao.getId());
        Assert.assertNotNull(salvo);
    }

    @Test
    public void deveriaAtualizarUmLeilao(){
        Usuario usuario = new UsuarioBuilder()
                .comNome("Fulano")
                .comEmail("fulano@email")
                .comSenha("12345678")
                .criar();
        em.persist(usuario);
        Leilao leilao = new LeilaoBuilder()
                .comNome("Mochila")
                .comValorInicial("500")
                .comData(LocalDate.now())
                .comUsuario(usuario)
                .criar();

        leilao = leilaoDao.salvar(leilao);

        leilao.setNome("Celular");
        leilao.setValorInicial(new BigDecimal("90"));

        leilao = leilaoDao.salvar(leilao);

        Leilao salvo = leilaoDao.buscarPorId(leilao.getId());
        Assert.assertEquals("Celular", salvo.getNome());
        Assert.assertEquals(new BigDecimal("90"), salvo.getValorInicial());
    }


}
