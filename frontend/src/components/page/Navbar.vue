<template>
    <div class="bg-gray-900">
        <!-- main menu -->
        <div class="max-w-8xl mx-auto px-3 sm:px-6 lg:px-9">
            <div class="relative flex justify-between items-center h-16">
                <!-- burger menu -->
                <div class="absolute left-0 flex items-center sm:hidden inset-y-0">
                    <button
                        class="inline-flex items-center justify-center p-2 rounded text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
                        @click="showItemsDropDown = !showItemsDropDown"
                    >
                        <svg
                            v-show="!showItemsDropDown"
                            class="h-6 w-6"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                            aria-hidden="true"
                        >
                            <path
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                stroke-width="2"
                                d="M4 6h16M4 12h16M4 18h16"
                            />
                        </svg>
                        <svg
                            v-show="showItemsDropDown"
                            class="h-6 w-6"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                            aria-hidden="true"
                        >
                            <path
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                stroke-width="2"
                                d="M6 18L18 6M6 6l12 12"
                            />
                        </svg>
                    </button>
                </div>
                <!-- logo and links -->
                <div class="flex flex-1 justify-center items-center sm:items-stretch sm:justify-start">
                    <div class="flex flex-shrink-0 items-center">
                        <img
                            class="h-8 w-auto"
                            src="@/assets/logo.png"
                        >
                        <span class="hidden sm:block text-white pl-4 font-bold text-lg">TimeGuess</span>
                        <span class="sm:hidden text-white pl-4 font-bold text-lg">TG</span>
                    </div>
                    <div class="hidden sm:block ml-10">
                        <div class="flex space-x-4 text-gray-300 text-sm font-medium">
                            <router-link
                                to="/"
                                class="hover:bg-gray-700 hover:text-white px-3 py-2 rounded"
                                active-class="bg-gray-700 text-white"
                                exact-path
                            >
                                {{ $t('generic.overview') }}
                            </router-link>
                            <a
                                href="#"
                                class="hover:bg-gray-700 hover:text-white px-3 py-2 rounded"
                            >Link 1</a>
                            <a
                                href="#"
                                class="hover:bg-gray-700 hover:text-white px-3 py-2 rounded"
                            >Link 2</a>
                        </div>
                    </div>
                </div>
                <!-- profile and dropdown -->
                <div class="absolute right-0 inset-y-0 flex items-center pr-3 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                    <div class="ml-3 relative">
                        <div>
                            <button
                                class="bg-gray-900 flex rounded-full focus:outline-none text-sm focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
                                @click="showProfileDropDown = !showProfileDropDown"
                            >
                                <img
                                    class="h-8 w-8 rounded-full"
                                    src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
                                >
                            </button>
                        </div>
                        <div
                            v-show="showProfileDropDown"
                            class="absolute right-0 origin-top-right mt-2 w-40 rounded shadow-lg py-2 bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
                        >
                            <a
                                href="#"
                                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-200"
                                @click="showProfileDropDown = false"
                            >Profile</a>
                            <a
                                href="#"
                                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-200"
                                @click="showProfileDropDown = false"
                            >Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- extended mobile menu -->
        <div
            v-show="showItemsDropDown"
            class="px-3 py-2 space-y-1 sm:hidden text-gray-300 font-medium"
        >
            <router-link
                to="/"
                class="hover:bg-gray-700 hover:text-white px-3 py-2 rounded block"
                active-class="bg-gray-700 text-white"
                exact-path
            >
                {{ $t('generic.overview') }}
            </router-link>
            <a
                href="#"
                class="hover:bg-gray-700 hover:text-white px-3 py-2 rounded block"
            >Link 1</a>
            <a
                href="#"
                class="hover:bg-gray-700 hover:text-white px-3 py-2 rounded block"
            >Link 2</a>
        </div>
    </div>
</template>
<script>
export default {
    name: 'Navbar',
    data() {
        return {
            showProfileDropDown: false,
            showItemsDropDown: false,
        };
    },
};
</script>
