//Fabiola Cortés Acuña - 21-822-463-6 ICCI
package poo;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Taller1 {

	static int limite = 100;

	static String[] alumnoNombre = new String[limite];
	static String[] alumnoApellido = new String[limite];
	static String[] alumnoRut = new String[limite];
	static String[] alumnoParalelo = new String[limite];
	static int[] alumnoM = new int[limite]; 
	
	static int cantA = 0;

	static String[] solicitudNombre = new String[limite];
	static String[] solicitudApellido = new String[limite];

	static int cantSolicitudes = 0;

	static String[] rechazadoNombre = new String[limite];
	static String[] rechazadoApellido = new String[limite];
	static String[] rechazadoRut = new String[limite];
	
	static int[] rechazadoSoloRut = new int[limite]; 
	
	static int cantRechazados = 0;

	static int versionRechazados = 0;

	static int totalIntentos = 0;
	static int totalAdmitidos = 0;
	static int admitidosManual = 0;

	static int admitidosAutomatico = 0;

	static int totalSoloRut = 0;
	static int cantDuplicadas = 0;


	static int archivosCargados = 0;

	static int versionC1 = 0;
	static int versionC2 = 0;

	static Scanner input = new Scanner(System.in);

	public static void opcionUno() {

		cantA = cargarAlumnos("Alumnos.txt");
		cantSolicitudes = cargarSolicitudes("Solicitudes.txt");

		int okAlum = 0;
		if (cantA >= 0) {
			okAlum = 1;
		}

		int okSol = 0;
		if (cantSolicitudes >= 0) {
			okSol = 1;
		}

		if (okAlum == 1) {
			if (okSol == 1) {
				archivosCargados = 1;
				System.out.println("Archivos cargados con exito!");
				System.out.println("- " + cantA + " alumnos en la lista.");
				System.out.println("- " + cantSolicitudes + " solicitudes de ingreso.");
			}
		}
	}

	public static int cargarAlumnos(String nombreArchivo) {

		File archivo = new File(nombreArchivo);
		
		int cantidad = 0;
		int lleno = 0;

		try {
			Scanner lector = new Scanner(archivo);

			while (lector.hasNextLine()) {
				String linea = lector.nextLine();

				if (!linea.equals("")) {

					if (cantidad < limite) {
						String[] partes = linea.split(";");

						if (partes.length == 4) {
							alumnoNombre[cantidad] = partes[0];
							alumnoApellido[cantidad] = partes[1];
							alumnoRut[cantidad] = partes[2];
							alumnoParalelo[cantidad] = partes[3];
							alumnoM[cantidad] = 0;
							cantidad = cantidad + 1;
							
						} else {
							System.out.println("Linea con formato invalido en " + nombreArchivo + ": " + linea);
						}
					} else if (lleno == 0) {
						
						System.out.println("Se alcanzo la capacidad maxima de " + limite + " alumnos. El resto del archivo no se cargo.");
						
						lleno = 1;
					}
				}
			}

			lector.close();
			return cantidad;

		} catch (IOException e) {
			System.out.println("No se pudo abrir el archivo " + nombreArchivo + ". Verifica que exista.");
			
			return -1;
		}
	}

	public static int cargarSolicitudes(String nombreArchivo) {

		File archivo = new File(nombreArchivo);
		int cantidad = 0;
		int lleno = 0;

		try {
			Scanner lector = new Scanner(archivo);

			while (lector.hasNextLine()) {
				String linea = lector.nextLine();

				if (!linea.equals("")) {

					if (cantidad < limite) {
						String[] partes = linea.split("-");

						if (partes.length == 2) {
							
							solicitudNombre[cantidad] = partes[0];
							solicitudApellido[cantidad] = partes[1];
							cantidad = cantidad + 1;
							
						} else {
							System.out.println("Linea con formato invalido en " + nombreArchivo + ": " + linea);
						}
						
					} else if (lleno == 0) {
						System.out.println("Se alcanzo la capacidad maxima de " + limite + " solicitudes. El resto del archivo no se cargo.");
						
						lleno = 1;
					}
				}
			}

			lector.close();
			return cantidad;

		} catch (IOException e) {
			System.out.println("No se pudo abrir el archivo " + nombreArchivo + ". Verifica que exista.");
			
			return -1;
		}
	}

	public static void opcionDos() {

		if (archivosCargados == 0) {
			System.out.println("Primero debes cargar los archivos (opcion 1).");
			return;
		}

		System.out.println("Procesando solicitudes...\n");

		int admitidosProceso = 0;
		int rechazadosProceso = 0;

		for (int i = 0; i < cantSolicitudes; i++) {

			String nombre = solicitudNombre[i];
			String apellido = solicitudApellido[i];
			totalIntentos = totalIntentos + 1;

			int indiceAlumno = buscarNombre(nombre, apellido);

			if (indiceAlumno != -1) {
				
				if (alumnoM[indiceAlumno] == 1) {
					
					System.out.println("Duplicado: " + nombre + " " + apellido + " ya habia sido admitido antes.");
					cantDuplicadas = cantDuplicadas + 1;
				} else {
					alumnoM[indiceAlumno] = 1;
					totalAdmitidos = totalAdmitidos + 1;
					admitidosAutomatico = admitidosAutomatico + 1;
					admitidosProceso = admitidosProceso + 1;
					System.out.println("Admitido: " + nombre + " " + apellido + " en " + alumnoParalelo[indiceAlumno]);
				}
			} else {

				int indiceRechazado = nomRechazado(nombre, apellido);

				if (indiceRechazado != -1) {
					
					System.out.println("Duplicado: " + nombre + " " + apellido + " ya figuraba como rechazado.");
					cantDuplicadas = cantDuplicadas + 1;
					
				} else if (cantRechazados < limite) {
					
					rechazadoNombre[cantRechazados] = nombre;
					rechazadoApellido[cantRechazados] = apellido;
					rechazadoRut[cantRechazados] = "";
					rechazadoSoloRut[cantRechazados] = 0;
					cantRechazados = cantRechazados + 1;
					rechazadosProceso = rechazadosProceso + 1;
					System.out.println("Rechazado: " + nombre + " " + apellido + " no pertenece a ningun paralelo.");
				} else {
					
					System.out.println("No hay espacio para registrar mas rechazados.");
				}
			}
		}

		System.out.println();
		System.out.println("Resumen: " + admitidosProceso + " admitidos / " + rechazadosProceso + " rechazados.");
	}

	public static void inscribirPorNombre() {

		System.out.println("Ingrese nombre: ");
		String nombre = input.nextLine();
		System.out.println("Ingrese apellido: ");
		String apellido = input.nextLine();

		totalIntentos = totalIntentos + 1;
		int indiceAlumno = buscarNombre(nombre, apellido);

		if (indiceAlumno == -1) {
			System.out.println(nombre + " " + apellido + " no pertenece a ningun paralelo del curso.");

			int indiceRechazado = nomRechazado(nombre, apellido);
			if (indiceRechazado == -1) {
				if (cantRechazados < limite) {
					rechazadoNombre[cantRechazados] = nombre;
					rechazadoApellido[cantRechazados] = apellido;
					rechazadoRut[cantRechazados] = "";
					rechazadoSoloRut[cantRechazados] = 0;
					cantRechazados = cantRechazados + 1;
				}
			}
			
			return;
		}

		if (alumnoM[indiceAlumno] == 1) {
			System.out.println(nombre + " " + apellido + " ya eres miembro del grupo.");
			
			
			return;
		}

		alumnoM[indiceAlumno] = 1;
		totalAdmitidos = totalAdmitidos + 1;
		admitidosManual = admitidosManual + 1;
		System.out.println(nombre + " " + apellido + " fue inscrito en el grupo, paralelo " + alumnoParalelo[indiceAlumno] + ".");
	}

	public static void inscribirPorRut() {

		System.out.println("Ingrese RUT: ");
		String rut = input.nextLine();

		totalIntentos = totalIntentos + 1;
		int indiceAlumno = buscarRut(rut);

		if (indiceAlumno == -1) {
			System.out.println("El RUT " + rut + " no pertenece a ningun paralelo del curso.");
			System.out.println("No tenemos su nombre, por lo que se registrara solo el RUT en los rechazados.");

			if (cantRechazados < limite) {
				rechazadoNombre[cantRechazados] = "";
				rechazadoApellido[cantRechazados] = "";
				rechazadoRut[cantRechazados] = rut;
				rechazadoSoloRut[cantRechazados] = 1;
				cantRechazados = cantRechazados + 1;
				totalSoloRut = totalSoloRut + 1;
			}
			
			return;
		}

		if (alumnoM[indiceAlumno] == 1) {
			System.out.println(alumnoNombre[indiceAlumno] + " " + alumnoApellido[indiceAlumno] + " ya es miembro del grupo.");
			return;
		}

		alumnoM[indiceAlumno] = 1;
		totalAdmitidos = totalAdmitidos + 1;
		admitidosManual = admitidosManual + 1;
		System.out.println(alumnoNombre[indiceAlumno] + " " + alumnoApellido[indiceAlumno] + " fue inscrito en el grupo, paralelo " + alumnoParalelo[indiceAlumno] + ".");
	}

	public static void opcionTres() {

		if (archivosCargados == 0) {
			System.out.println("Primero debes cargar los archivos (opcion 1).");
			return;
		}

		System.out.println("Como desea inscribir a la persona?");
		System.out.println("1) Por nombre completo");
		System.out.println("2) Por RUT");
		System.out.println("Ingrese opcion: ");

		String linea = input.nextLine();

		if (linea.equals("1")) {
			inscribirPorNombre();
		} else if (linea.equals("2")) {
			inscribirPorRut();
		} else {
			System.out.println("Opcion invalida.");
		}
	}
    public static void cambiarParalelo() {

		System.out.println("Ingrese RUT del alumno: ");
		String rut = input.nextLine();

		int indice = buscarRut(rut);

		if (indice == -1) {
			System.out.println("RUT INVALIDO: No existe ningun alumno con ese RUT.");
			
			return;
		}

		System.out.println("Alumno: " + alumnoNombre[indice] + " " + alumnoApellido[indice] + " (actualmente en " + alumnoParalelo[indice] + ")");
		System.out.println("Nuevo paralelo (C1/C2): ");
		String nuevoParalelo = input.nextLine();

		if (validoC1C2(nuevoParalelo) == 0) {
			System.out.println("Paralelo invalido, solo deben ser C1 o C2.");
			return;
		}

		nuevoParalelo = nuevoParalelo.toUpperCase();
		alumnoParalelo[indice] = nuevoParalelo;
		guardarAlumnos();
		System.out.println("¡Paralelo actualizado! Cambios guardados en Alumnos.txt");
	}

	public static void paraEliminar() {

		System.out.println("Ingrese RUT de alumno que eliminara: ");
		String rut = input.nextLine();

		int indice = buscarRut(rut);

		if (indice == -1) {
			System.out.println("RUT INVALIDO: No existe ningun alumno con ese RUT");
			
			return;
		}

		System.out.println("Se eliminara a " + alumnoNombre[indice] + " " + alumnoApellido[indice] + " del curso.");

		for (int i = indice; i < cantA - 1; i++) {
			alumnoNombre[i] = alumnoNombre[i + 1];
			alumnoApellido[i] = alumnoApellido[i + 1];
			alumnoRut[i] = alumnoRut[i + 1];
			alumnoParalelo[i] = alumnoParalelo[i + 1];
			alumnoM[i] = alumnoM[i + 1];
		}

		cantA = cantA - 1;
		guardarAlumnos();
		System.out.println("Alumno eliminado, si era miembro del grupo, perdio el acceso, Cambios guardados en Alumnos.txt");
	}

	public static void inscribirAlumnoNuevo() {

		if (cantA >= limite) {
			System.out.println("La inscripción no es posible debido a cupos no disponibles");
			return;
		}

		System.out.println("Ingrese su nombre: ");
		String nombre = input.nextLine();
		System.out.println("Ingrese su apellido: ");
		String apellido = input.nextLine();
		System.out.println("Ingrese su RUT: ");
		String rut = input.nextLine();
		System.out.println("Ingrese paralelo su (C1/C2): ");
		String paralelo = input.nextLine();

		if (nombre.equals("")) {
			System.out.println("Debes agregar algo, NO dejarlo vacio");
			return;
		}

		if (apellido.equals("")) {
			System.out.println("Debes agregar algo, no dejarlo vacio");
			return;
		}

		if (rut.equals("")) {
			System.out.println("Los campos no pueden quedar en blanco.");
			return;
		}

		if (validoC1C2(paralelo) == 0) {
			System.out.println("Paralelo invalido. Debe ser C1 o C2.");
			return;
		}

		paralelo = paralelo.toUpperCase();

		if (buscarRut(rut) != -1) {
			System.out.println("Ya existe un alumno inscrito con ese RUT.");
			return;
		}

		alumnoNombre[cantA] = nombre;
		alumnoApellido[cantA] = apellido;
		alumnoRut[cantA] = rut;
		alumnoParalelo[cantA] = paralelo;
		alumnoM[cantA] = 0;
		cantA = cantA + 1;

		guardarAlumnos();
		System.out.println("Alumno inscrito en la lista del curso. Aun debe procesarse o inscribirse para entrar al grupo.");
	} public static void guardarAlumnos() {

		try {
			FileWriter achReescrito = new FileWriter("Alumnos.txt");
			BufferedWriter reescribir = new BufferedWriter(achReescrito);

			for (int i = 0; i < cantA; i++) {
				reescribir.write(alumnoNombre[i] + ";" + alumnoApellido[i] + ";" + alumnoRut[i] + ";" + alumnoParalelo[i]);
				reescribir.newLine();
			}

			reescribir.close();

		} catch (IOException e) {
			System.out.println("No se pudo guardar Alumnos.txt.");
		}
	}

	public static void opcionCuatro() {

		if (archivosCargados == 0) {
			System.out.println("Primero debes cargar los archivos (opcion 1).");
			return;
		}

		int volver = 0;

		while (volver == 0) {
			System.out.println();
			System.out.println("--- Administracion del curso ---");
			System.out.println("1) Cambiar paralelo de un alumno");
			System.out.println("2) Eliminar alumno del curso");
			System.out.println("3) Inscribir alumno nuevo");
			System.out.println("4) Volver");
			System.out.println("Ingrese opcion: ");

			String linea = input.nextLine();

			if (linea.equals("1")) {
				cambiarParalelo();
			} else if (linea.equals("2")) {
				paraEliminar();
			} else if (linea.equals("3")) {
				inscribirAlumnoNuevo();
			} else if (linea.equals("4")) {
				volver = 1;
			} else {
				System.out.println("Opcion invalida.");
			}
		}
	} public static void reporteParalelo(String paralelo) {

		int version;
		if (paralelo.equals("C1")) {
			versionC1 = versionC1 + 1;
			version = versionC1;
		} else {
			versionC2 = versionC2 + 1;
			version = versionC2;
		}

		String nombreArchivo = "Reportes/Reporte" + paralelo + "-V" + version + ".txt";

		try {
			FileWriter achReescrito = new FileWriter(nombreArchivo);
			BufferedWriter reescribir = new BufferedWriter(achReescrito);

			reescribir.write("=== Miembros del grupo - Paralelo " + paralelo + " ===");
			reescribir.newLine();

			for (int i = 0; i < cantA; i++) {
				if (alumnoM[i] == 1) {
					if (alumnoParalelo[i].equals(paralelo)) {
						reescribir.write(alumnoNombre[i] + " " + alumnoApellido[i] + " - " + alumnoRut[i]);
						reescribir.newLine();
					}
				}
			}

			reescribir.close();
			System.out.println("Reporte generado: " + nombreArchivo);

		} catch (IOException e) {
			System.out.println("No se pudo generar el reporte de " + paralelo + ".");
		}
	}

	public static void reporteRechazados() {

		versionRechazados = versionRechazados + 1;
		String nombreArchivo = "Reportes/Rechazados-V" + versionRechazados + ".txt";

		try {
			FileWriter achReescrito = new FileWriter(nombreArchivo);
			BufferedWriter reescribir = new BufferedWriter(achReescrito);

			reescribir.write("=== Solicitudes rechazadas ===");
			reescribir.newLine();

			for (int i = 0; i < cantRechazados; i++) {
				if (rechazadoSoloRut[i] == 1) {
					reescribir.write("Sin nombre registrado, RUT: " + rechazadoRut[i]);
				} else {
					reescribir.write(rechazadoNombre[i] + " " + rechazadoApellido[i] + " - No pertenece a ningun paralelo del curso");
				}
				reescribir.newLine();
			}

			reescribir.close();
			System.out.println("Reporte generado: " + nombreArchivo);

		} catch (IOException e) {
			System.out.println("No se pudo generar el reporte de rechazados.");
		}
	}

	public static void opcionCinco() {

		if (archivosCargados == 0) {
			System.out.println("Primero debes cargar los archivos (opcion 1).");
			return;
		}

		int volver = 0;

		while (volver == 0) {
			System.out.println();
			System.out.println("--- Generar reportes ---");
			System.out.println("1) Reporte paralelo C1");
			System.out.println("2) Reporte paralelo C2");
			System.out.println("3) Reporte de rechazados");
			System.out.println("4) Volver");
			System.out.println("Ingrese opcion: ");

			String linea = input.nextLine();

			if (linea.equals("1")) {
				reporteParalelo("C1");
			} else if (linea.equals("2")) {
				reporteParalelo("C2");
			} else if (linea.equals("3")) {
				reporteRechazados();
			} else if (linea.equals("4")) {
				volver = 1;
			} else {
				System.out.println("Opcion invalida.");
			}
		}
	}


	
	public static String mayorApellido() {

		if (cantA == 0) {
			return null;
		}

		String apellidoGanador = alumnoApellido[0];
		int mayor = 0;

		for (int i = 0; i < cantA; i++) {
			int repeticiones = 0;

			for (int j = 0; j < cantA; j++) {
				if (alumnoApellido[j].equals(alumnoApellido[i])) {
					repeticiones = repeticiones + 1;
				}
			}

			if (repeticiones > mayor) {
				mayor = repeticiones;
				apellidoGanador = alumnoApellido[i];
			}
		}

		return apellidoGanador;
	}

	public static void opcionSeis() {

		if (archivosCargados == 0) {
			System.out.println("Primero debes cargar los archivos (opcion 1).");
			return;
		}

		System.out.println();
		System.out.println("--- Analisis estadistico ---");
		System.out.println("Total de intentos de ingreso: " + totalIntentos);

		if (totalIntentos == 0) {
			System.out.println("Aun no hay intentos de ingreso registrados.");
		} else {
			double porcentajeRechazo = (cantRechazados * 100.0) / totalIntentos;
			double tasaAdmision = (totalAdmitidos * 100.0) / totalIntentos;
			System.out.println("Rechazados: " + cantRechazados + " (" + porcentajeRechazo + "%)");
			System.out.println("Tasa de admision: " + tasaAdmision + "%");
		}

	
		
		int cantC1 = 0;
		int cantC2 = 0;
		for (int i = 0; i < cantA; i++) {
			if (alumnoParalelo[i].equals("C1")) {
				cantC1 = cantC1 + 1;
			} else if (alumnoParalelo[i].equals("C2")) {
				cantC2 = cantC2 + 1;
			}
		}

		System.out.println("Alumnos en la lista: " + cantA + " (C1: " + cantC1 + ", C2: " + cantC2 + ")");

		if (cantA > 0) {
			double porcentajeC1 = (cantC1 *100.0 ) / cantA;
			double porcentajeC2 = (cantC2 *100.0 ) / cantA;
			System.out.println("Porcentaje C1: " + porcentajeC1 + "%  |  Porcentaje C2: " + porcentajeC2 + "%");

			if (cantC1 > cantC2) {
				System.out.println("Paralelo con mas alumnos es: C1");
			} else if (cantC2 > cantC1) {
				System.out.println("Paralelo con mas alumnos es: C2");
			} else {
				System.out.println("Ambos paralelos tienen la misma cantidad de alumnos.");
			}
		}

		System.out.println("Ingresos solo con RUT (anonimos) entre los rechazados: " + totalSoloRut);
		System.out.println("Inscripciones manuales: " + admitidosManual + "  |  Inscripciones automaticas: " + admitidosAutomatico);
		System.out.println("Solicitudes duplicadas detectadas: " + cantDuplicadas);

		String apellidoGanador = mayorApellido();
		if (apellidoGanador != null) {
			System.out.println("Apellido mas repetido en la lista del curso: " + apellidoGanador);
		}
	}
	public static int buscarNombre(String nombre, String apellido) {

		for (int i = 0; i < cantA; i++) {
			if (alumnoNombre[i].equalsIgnoreCase(nombre)) {
				if (alumnoApellido[i].equalsIgnoreCase(apellido)) {
					return i;
				}
			}
		}
		return -1;
	}
	public static int buscarRut(String rut) {

		for (int i = 0; i < cantA; i++) {
			if (alumnoRut[i].equalsIgnoreCase(rut)) {
				return i;
			}
		}
		return -1;
	}

	
	public static int nomRechazado(String nombre, String apellido) {

		for (int i = 0; i < cantRechazados; i++) {
			if (rechazadoSoloRut[i] == 0) {
				if (rechazadoNombre[i].equalsIgnoreCase(nombre)) {
					if (rechazadoApellido[i].equalsIgnoreCase(apellido)) {
						return i;
					}
				}
			}
		}
		
		return -1;
	}

	public static int validoC1C2(String paralelo) {
		if (paralelo.equalsIgnoreCase("C1")) {
			return 1;
		}
		if (paralelo.equalsIgnoreCase("C2")) {
			return 1;
		}
		return 0;
	}


	public static void main(String[] args) {

		int escogido = opcion();

		while (escogido != 7) {

			if (escogido == 1) {
				opcionUno();

			} else if (escogido == 2) {
				opcionDos();

			} else if (escogido == 3) {
				opcionTres();

			} else if (escogido == 4) {
				opcionCuatro();

			} else if (escogido == 5) {
				opcionCinco();

			} else if (escogido == 6) {
				opcionSeis();
			}

			escogido = opcion();
		}

		System.out.println("Saliendo del sistema. Hasta luego!");
		input.close();
	}

	public static int opcion() {

		System.out.println();
		System.out.println("===== Sistema de Control del Grupo POO =====");
		System.out.println("1) Cargar archivos (Alumnos y Solicitudes)");
		System.out.println("2) Procesar solicitudes (Filtrado automatico)");
		System.out.println("3) Inscripcion manual al grupo");
		System.out.println("4) Administracion del curso");
		System.out.println("5) Generar reportes");
		System.out.println("6) Analisis estadistico");
		System.out.println("7) Salir");

		int escogido = -1;
		int valido = 0;

		while (valido == 0) {
			System.out.println("Ingrese opcion escogida: ");

			try {
				escogido = Integer.valueOf(input.nextLine());

				if (escogido >= 1) {
					if (escogido <= 7) {
						valido = 1;
					}
				}

				if (valido == 0) {
					System.out.println("El numero escogido es incorrecto, debe estar entre 1 y 7.");
				}
			} catch (Exception e) {
				System.out.println("Debes ingresar un numero entre 1 y 7.");
			}
		}

		return escogido;
	}
}
