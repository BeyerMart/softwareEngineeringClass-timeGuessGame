<template>
    <div class="my-2 rounded shadow">
        <h1 class="text-xl">
            {{ message }}
        </h1>
        <label class="text-reader">
            Read File
            <input
                type="file"
                accept="application/json"
                @change="onFileChange"
            >
        </label>
    </div>
</template>

<script>
import { createTerm } from '@/services/topic.service';

export default {
    data() {
        return {
            jsonData: [],
            message: '',
        };
    },
    methods: {
        // Source: https://stackoverflow.com/questions/54617844/using-filereader-to-read-a-json-file
        onFileChange(e) {
            const files = e.target.files || e.dataTransfer.files;
            if (!files.length) return;
            this.readFile(files[0]);
        },
        readFile(file) {
            const reader = new FileReader();
            reader.readAsText(file);

            reader.onload = (e) => {
                this.jsonData = JSON.parse(e.target.result);
                this.createTerms();
            };
        },

        createTerms() {
            this.jsonData.every((term) => {
                createTerm(4, { name: term }).then((res) => {
                    this.message = `Term: ${res.data.name} was added`;
                }).catch(() => {
                    this.message = 'Something went wrong';
                });
                return true;
            });
        },

    },
};
</script>

<style>
.list-item {
  display: inline-block;
  margin-right: 10px;
}
.list-enter-active, .list-leave-active {
  transition: all 1s;
}
.list-enter, .list-leave-to /* .list-leave-active below version 2.1.8 */ {
  opacity: 0;
  transform: translateY(30px);
}
.text-reader {
  position: relative;
  overflow: hidden;
  display: inline-block;

  /* Fancy button looking */
  border: 2px solid black;
  border-radius: 5px;
  padding: 8px 12px;
  cursor: pointer;
}
.text-reader input {
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  opacity: 0;
}
</style>
