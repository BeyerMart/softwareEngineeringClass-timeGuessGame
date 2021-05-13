<template>
    <div class="bg-gray-900 py-8 px-16 flex flex-col text-white justify-center items-center sm:items-start sm:flex-row">
        <div class="text-2xl font-bold text-right">
            TimeGuess
        </div>
        <div class="sm:ml-10 text-gray-300 font-medium">
            <a
                :href="'https://www.uibk.ac.at/index.html.' + $i18n.locale"
                class="hover:text-white block"
            >{{ $t('generic.universityOfInnsbruck') }}</a>
            <a
                href="#"
                class="hover:text-white block"
            >{{ $t('generic.tos') }}</a>
            <a
                href="#"
                class="hover:text-white block"
            >{{ $t('generic.imprint') }}</a>
        </div>
    </div>
</template>
<script>
export default {
    name: 'Footer',
};
</script>
