type Metodo = (...args: unknown[]) => unknown;

/**
 * Função que aplica debounce.
 * @param tempo Tempo de debounce.
 */
export function debounce(tempo = 300) {
    return function (
        target: unknown,
        propertyKey: string,
        descriptor: PropertyDescriptor
    ) {
        const metodoOriginal: Metodo = descriptor.value;
        let timer = 0;

        // Sobrescrevendo função
        descriptor.value = function (...args: unknown[]) {
            window.clearTimeout(timer);
            timer = window.setTimeout(() => {
                metodoOriginal.apply(this, args);
            }, tempo);
        };
    };
}
