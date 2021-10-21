package org.example.site.action;

import org.example.dao.PessoaDAO;
import org.example.entidade.Pessoa;
import org.example.hibernate.HibernateUtil;
import org.example.util.CassUtil;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class _testes {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSession();

        PessoaDAO pessoaDAO = new PessoaDAO(session);
        Calendar hoje = Calendar.getInstance();

        Pessoa pessoa = new Pessoa("Teste", hoje, "123456789");
//        pessoaDAO.salvar(pessoa);

//        List<Pessoa> lista = pessoaDAO.listar();
//        for (Pessoa p: lista) {
//            System.out.println(p);
//        }
//        System.out.println("---------------------------------------------------------------------------");
//        List<Map<String, Object>> pessoas = pessoaDAO.liste();
//        for (Map<String, Object> p: pessoas) {
//            System.out.println(p);
//        }
    }
}
