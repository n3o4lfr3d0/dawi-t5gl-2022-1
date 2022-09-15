package app;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Usuario;

public class Demo01 {

	// registrar los datos de un nuevo usuario
	public static void main(String[] args) {
			// establecer conexion --> con la unidad de persistencia
			EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("mysql");
			// crea el manejador de entidades
			EntityManager em = fabrica.createEntityManager();
			
			// proceso -> reg, act, eli --> transacciones
			em.getTransaction().begin();
			
			// obj a grabar
			Usuario u = new Usuario(20, "Maria", "Cruz", "mcruz", "567", "2001/11/04", 2, 1, null);
			em.persist(u);
			
			System.out.println("Grabación Ok");
			
			// confirmar la transacción
			em.getTransaction().commit();
			
			// cerrar la conexion
			em.close();			
	}
}
