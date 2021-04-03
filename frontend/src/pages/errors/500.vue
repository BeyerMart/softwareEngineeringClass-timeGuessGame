<template>
    <div class="w-full h-full flex">
        <div class="m-auto flex flex-col">
            <span class="text-7xl sm:text-9xl font-bold text-gray-900 text-center">500</span>
            <span class="mb-10 text-gray-900 text-center">{{ $t('errors.internalserror') }}</span>

            <router-link
                class="bg-gray-900 text-white px-4 py-2 rounded text-center cursor-pointer mx-10"
                to="/"
            >
                {{ $t('generic.overview') }}
            </router-link>
        </div>
    </div>
</template>
