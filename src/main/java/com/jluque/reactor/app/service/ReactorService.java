package com.jluque.reactor.app.service;

public interface ReactorService {

	/**
	 * Example Map. carga lista de nombres los separa por nombre y apellido, filtra
	 * y cambia mayusculas/minusculas.
	 * 
	 * Subscribe y muestra resutaldos.
	 * 
	 * @throws Exception
	 */
	public void iterableMap();

	/**
	 * example FlatMap. replcia del item iterableMap, pero usando flatMap, aplano
	 * las listas y las filtro en el mismo flujo.
	 * 
	 * Al final subscribe y muestra resultados.
	 * 
	 * @throws Exception
	 */
	public void iterableFlatMap();

	/**
	 * Cambia objetos de Flux a Mono usando collectList
	 * 
	 * Subscribe y muestra resultadosO
	 * 
	 * @throws Exception
	 */
	public void usersfluxToMonoMapping();

	/**
	 * Convierte un obejto Dto en una lista de String.
	 * 
	 * Subcribe y muestra resultados.
	 */
	public void iterableToStringMapping();

	/**
	 * Lanzamos posteos de comentarios uniendo Usuarios y Comentarios usando
	 * flatMap.
	 * 
	 * Subscribe e imprime resultados
	 */
	public void postCommentsFlatMap();

	/**
	 * Misma situacion que postCommentsFlatMap pero usando zipWith Single parameter
	 * para relacionar Usuarios y Comentarios en un dto que se llama posteDto
	 * 
	 * Subscribe e imprime resultados
	 */
	public void postCommentsZipWith();

	/**
	 * Misma situacion que postCommentsFlatMap pero usando zipWith usando bifunciton
	 * con dos parametros para relacionar Usuarios y Comentarios en un dto que se
	 * llama posteDto
	 * 
	 * Subscribe e imprime resultados
	 */
	public void postCommentsZipWithBifunction();

	/**
	 * Usa rango para decidir cantidad a imprimir. Combina dos flux usando zipWith.
	 * 
	 * @Flux1: lista de enteros y procesados.
	 * @Flux2: usa un rango de dicha lista.
	 * 
	 * @Subscribe: lista general
	 */
	public void range();

	/**
	 * Usa un intervalo de Flux para frenar segun el rango para decidir cantidad a
	 * imprimir. Combina dos flux usando zipWith.
	 * 
	 * @Flux1: un rango de conteo.
	 * @Flux2: es un delay para cada elemento del rango.
	 * 
	 * @Subscribe: lista general
	 */
	public void interval();

	/**
	 * Misma situacion que @interval() Usa un intervalo con delayElements de Flux
	 * para frenar segun el rango para decidir cantidad a imprimir.
	 * 
	 * @Flux: un rango de conteo. Agrega delay
	 * 
	 * @Subscribe: NoBoqueante, hilos corriendo en background. Interacalamos
	 *             con @BlockLast.
	 * @BlockLast: Bloqueante. No recomendado ya que genera cuellos de botella.
	 *             hilos corriendo en rpimer plano. Intercala con @Subscribe
	 */
	public void delayElement();

	/**
	 * rangos o intervalos infinitos controlados por un coutnDownLatch. Al llegar al
	 * quinto elemento, lanzamos un error forzado emulando un error de conexion por
	 * timeout. Ante errores, se controla el reintento.
	 * 
	 * @Subscribe: imprime, o lista general o error controlado.
	 * 
	 * @throws InterruptedException
	 */
	public void infiniteInterval() throws InterruptedException;

	/**
	 * Intervalo infinito de tiempo usando Flux.create. manejamos un contador
	 * manual, usamos Timer, cancelamos y completamos el flujo y controlamos error.
	 * 
	 * @Subscribe - a create y devuelve un flux de objetos.
	 */
	public void infiniteIntervalFromCreate();

	/**
	 * Itera un rango y pide un log para ver el subscribre background. Recibe y
	 * procesa el lote completo.
	 * 
	 */
	public void backPresureSimple();

	/**
	 * Control de presion, manejo de contra presion. Implementamos el metodo
	 * "onSubscribe" dentro del "subscribe" y manejamos los lotes a recibir con
	 * contadores y flags.
	 * 
	 */
	public void backPresure() throws InterruptedException;

	/**
	 * Igual que BackPresure, pero simplificamos la contrapresion con un limitRate.
	 * 
	 * @throws InterruptedException
	 */
	public void backPresureLimitRate() throws InterruptedException;

}
