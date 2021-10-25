/**
 * Função que aplica debounce.
 * @param tempo Tempo de debounce.
 */
export function debounce(tempo = 300) {
  return function(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    const metodoOriginal: Function = descriptor.value;
    let timer = 0;

    // Sobrescrevendo função
    descriptor.value = function(...args: any[]) {
      window.clearTimeout(timer);
      timer = window.setTimeout(() => {
        metodoOriginal.apply(this, args);
      }, tempo);
    }
  }
}