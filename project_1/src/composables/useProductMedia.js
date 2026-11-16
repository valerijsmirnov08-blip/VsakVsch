import { ref, computed } from 'vue';
export function useProductMedia(product)
{
    const activeImage = ref('');
    const isDragover = ref(false);
    const localPreviews = ref({});

    const galleryImages = computed(() => {
    if(!product.value || !product.value.details) return [];
    const getUrl = (img) => localPreviews.value[img] || '/' + img;
    const images = [];
    if(product.value.details.mainImage) {
        images.push(getUrl(product.value.details.mainImage));
    }
    if(product.value.details.gallery && Array.isArray(product.value.details.gallery)) {
        const extra = product.value.details.gallery;
        images.push(...extra.map(getUrl));
    }
    return images;
    });
    const handleMainImageDrop = (event) =>
    {
        isDragover.value = false;
        const file = Array.from(event.dataTransfer.files);
        
        if(!file.length)
        {
            return
        }
        file.forEach(file =>
        {
            const previewUrl = URL.createObjectURL(file);
            localPreviews.value[file.name] = previewUrl;
            
            if(!product.value.details.mainImage)
            {
                product.value.details.mainImage = file.name;
                activeImage.value = previewUrl;
            }
            else
            {
                if(!Array.isArray(product.value.details.gallery))
                {
                    product.value.details.gallery = product.value.details.gallery
                    ? product.value.details.gallery.split(',').map(s => s.trim()) : [];
                }
                product.value.details.gallery.push(file.name);
                activeImage.value = previewUrl;
            }
        });
    }
    return{
        activeImage,
        isDragover,
        galleryImages,
        handleMainImageDrop
    };
}

