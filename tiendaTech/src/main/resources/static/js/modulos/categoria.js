
document.addEventListener('DOMContentLoaded', function () {

    // Modal para agregar o actualizar categorías
    const modal = document.getElementById('categoriaModal');

    if (modal) {

        const form = modal.querySelector('form');
        const titulo = document.getElementById('categoriaModalLabel');
        const id = document.getElementById('idCategoria');
        const descripcion = document.getElementById('descripcion');
        const activo = document.getElementById('activo');
        const rutaImagen = document.getElementById('rutaImagen');
        const imagenFile = document.getElementById('imagenFile');

        modal.addEventListener('show.bs.modal', function (event) {

            const boton = event.relatedTarget;

            if (!boton) {
                return;
            }

            const modo = boton.getAttribute('data-modo');

            console.log('Modo:', modo);
            console.log('ID recibido:', boton.getAttribute('data-bs-id'));

            // Limpiar los datos anteriores del formulario
            form.reset();
            id.value = '';
            descripcion.value = '';
            rutaImagen.value = '';
            imagenFile.value = '';

            if (modo === 'editar') {

                // Actualizar una categoría existente
                titulo.textContent = 'Actualizar categoría';

                id.value = boton.getAttribute('data-bs-id') || '';

                descripcion.value =
                        boton.getAttribute('data-bs-descripcion') || '';

                activo.checked =
                        boton.getAttribute('data-bs-activo') === 'true';

                rutaImagen.value =
                        boton.getAttribute('data-bs-imagen') || '';

                console.log('ID cargado en formulario:', id.value);

            } else {

                // Agregar una categoría nueva
                titulo.textContent = 'Agregar categoría';
                activo.checked = true;

            }

        });

        // Evitar guardar una actualización sin identificador
        form.addEventListener('submit', function (event) {

            const idActual = id.value.trim();

            if (titulo.textContent === 'Actualizar categoría' &&
                    idActual === '') {

                event.preventDefault();

                alert('Error: no se recibió el ID de la categoría. No se guardaron los cambios.');

                console.error('Actualización cancelada: ID vacío.');
            }

        });
    }

    // Modal para confirmar la eliminación
    const confirmModal = document.getElementById('confirmModal');

    if (confirmModal) {

        confirmModal.addEventListener('show.bs.modal', function (event) {

            const boton = event.relatedTarget;

            if (!boton) {
                return;
            }

            const id = boton.getAttribute('data-bs-id');
            const descripcion = boton.getAttribute('data-bs-descripcion');

            document.getElementById('idCategoriaEliminar').value =
                    id || '';

            document.getElementById('descripcionEliminar').textContent =
                    descripcion || '';

        });
    }

});
